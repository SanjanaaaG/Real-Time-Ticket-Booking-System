package com.eventhub.booking_service.ServiceImpl;

import com.eventhub.booking_service.Client.EventServiceClient;
import com.eventhub.booking_service.DTO.BookingResponse;
import com.eventhub.booking_service.DTO.HoldRequest;
import com.eventhub.booking_service.DTO.HoldResponse;
import com.eventhub.booking_service.DTO.SeatDetailsResponse;
import com.eventhub.booking_service.Entity.Booking;
import com.eventhub.booking_service.Entity.BookingEvent;
import com.eventhub.booking_service.Entity.BookingSeat;
import com.eventhub.booking_service.Entity.BookingStatusEnum;
import com.eventhub.booking_service.Exception.BookingConflictException;
import com.eventhub.booking_service.Exception.ResourceNotFoundException;
import com.eventhub.booking_service.Exception.SeatUnavailableException;
import com.eventhub.booking_service.Repository.BookingRepo;
import com.eventhub.booking_service.Service.BookingEventPublisher;
import com.eventhub.booking_service.Service.BookingService;
import com.eventhub.booking_service.Service.SeatHoldService;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final SeatHoldService seatHoldService;
    private final BookingRepo bookingRepository;
    private final EventServiceClient eventServiceClient;
    private final BookingEventPublisher bookingEventPublisher;

    @Value("${booking.hold.duration-seconds}")
    private long holdDurationSeconds;

    // NOTE: price-per-seat is hardcoded here for now since Booking Service
    // hasn't called Event Service yet to fetch real seat prices. We'll wire
    // that REST call in next — for now this proves the locking logic works.
    private static final BigDecimal PLACEHOLDER_PRICE = BigDecimal.valueOf(1000);

    @Override
    @Transactional
    public HoldResponse holdSeats(HoldRequest request) {

        // 1. Fetch real seat data from Event Service via Feign
        List<SeatDetailsResponse> eventSeats = eventServiceClient.getSeatsForEvent(request.getEventId());

        Map<Long, SeatDetailsResponse> seatMap = eventSeats.stream()
                .collect(Collectors.toMap(SeatDetailsResponse::getSeatid, s -> s));

        // 2. Validate every requested seat exists and is AVAILABLE, before touching Redis at all
        for (Long seatId : request.getSeatIds()) {
            SeatDetailsResponse seat = seatMap.get(seatId);
            if (seat == null) {
                throw new SeatUnavailableException(
                        "Seat " + seatId + " does not exist for event " + request.getEventId());
            }
            if (!"AVAILABLE".equals(seat.getSeatStatus())) {
                throw new SeatUnavailableException(
                        "Seat " + seatId + " is already booked");
            }
        }

        // 3. Now attempt the Redis holds — same rollback logic as before
        List<Long> successfullyHeldSeatIds = new ArrayList<>();
        try {
            for (Long seatId : request.getSeatIds()) {
                boolean held = seatHoldService.tryHoldSeat(
                        request.getEventId(), seatId, request.getUserId());

                if (!held) {
                    rollbackHolds(request.getEventId(), successfullyHeldSeatIds);
                    throw new SeatUnavailableException(
                            "Seat " + seatId + " is no longer available");
                }
                successfullyHeldSeatIds.add(seatId);
            }

            // 4. Build booking using REAL prices from Event Service, not a placeholder
            BigDecimal totalAmount = request.getSeatIds().stream()
                    .map(seatId -> seatMap.get(seatId).getPrice())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Booking booking = Booking.builder()
                    .userId(request.getUserId())
                    .eventId(request.getEventId())
                    .status(BookingStatusEnum.HELD)
                    .totalAmount(totalAmount)
                    .build();

            List<BookingSeat> bookingSeats = request.getSeatIds().stream()
                    .map(seatId -> BookingSeat.builder()
                            .booking(booking)
                            .seatId(seatId)
                            .price(seatMap.get(seatId).getPrice())
                            .build())
                    .collect(Collectors.toList());

            booking.setBookingSeats(bookingSeats);
            Booking saved = bookingRepository.save(booking);

            return toResponse(saved);

        } catch (SeatUnavailableException ex) {
            throw ex;
        } catch (Exception ex) {
            rollbackHolds(request.getEventId(), successfullyHeldSeatIds);
            throw ex;
        }
    }

    private void rollbackHolds(Long eventId, List<Long> seatIds) {
        for (Long seatId : seatIds) {
            seatHoldService.releaseHold(eventId, seatId);
        }
    }

    private HoldResponse toResponse(Booking booking) {
        List<Long> seatIds = booking.getBookingSeats().stream()
                .map(BookingSeat::getSeatId)
                .collect(Collectors.toList());

        return HoldResponse.builder()
                .bookingId(booking.getBookingId())
                .eventId(booking.getEventId())
                .status(booking.getStatus())
                .seatIds(seatIds)
                .totalAmount(booking.getTotalAmount())
                .createdAt(booking.getCreatedAt())
                .holdExpiresAt(booking.getCreatedAt().plusSeconds(holdDurationSeconds))
                .build();
    }


    @Override
    @Transactional
    public BookingResponse confirmBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        // Idempotency guard — if it's already CONFIRMED, don't re-run side effects
        // (e.g. a double-click on "pay" shouldn't try to book already-booked seats).
        if (booking.getStatus() == BookingStatusEnum.CONFIRMED) {
            return toBookingResponse(booking);
        }

        if (booking.getStatus() != BookingStatusEnum.HELD) {
            throw new BookingConflictException(
                    "Booking " + bookingId + " cannot be confirmed — current status is " + booking.getStatus());
        }

        List<Long> seatIds = booking.getBookingSeats().stream()
                .map(BookingSeat::getSeatId)
                .collect(Collectors.toList());

        // 1. Verify every seat's hold is still valid in Redis before doing anything permanent.
        //    If the 5-minute TTL already expired, don't let a late payment sneak through.
        for (Long seatId : seatIds) {
            if (!seatHoldService.isHeldByUser(booking.getEventId(), seatId, booking.getUserId())) {
                throw new BookingConflictException(
                        "Hold on seat " + seatId + " has expired — please start a new booking");
            }
        }

        // 2. Confirm each seat in Event Service, with rollback if any seat fails partway through.
        List<Long> confirmedSeatIds = new ArrayList<>();
        try {
            for (Long seatId : seatIds) {
                try {
                    eventServiceClient.markSeatBooked(booking.getEventId(), seatId);
                    confirmedSeatIds.add(seatId);
                } catch (FeignException.Conflict ex) {
                    // Event Service's optimistic-lock backstop caught a genuine race —
                    // extremely rare given the Redis lock already gated this, but possible.
                    throw new BookingConflictException(
                            "Seat " + seatId + " was booked by another process — please retry");
                }
            }
        } catch (BookingConflictException ex) {
            rollbackConfirmedSeats(booking.getEventId(), confirmedSeatIds);
            throw ex;
        }

        // 3. Release the Redis holds now that seats are permanently booked in Event Service.
        for (Long seatId : seatIds) {
            seatHoldService.releaseHold(booking.getEventId(), seatId);
        }

        // 4. Persist the confirmed state.
        booking.setStatus(BookingStatusEnum.CONFIRMED);
        booking.setConfirmedAt(LocalDateTime.now());
        Booking saved = bookingRepository.save(booking);

        // TODO (Kafka step): publish BookingConfirmed event here for Notification Service.
        bookingEventPublisher.publish(BookingEvent.builder()
                .eventType("BOOKING_CONFIRMED")
                .bookingId(saved.getBookingId())
                .eventId(saved.getEventId())
                .userId(saved.getUserId())
                .seatIds(seatIds)
                .status(saved.getStatus())
                .totalAmount(saved.getTotalAmount())
                .timestamp(LocalDateTime.now())
                .build());

        return toBookingResponse(saved);
    }

    private void rollbackConfirmedSeats(Long eventId, List<Long> seatIds) {
        for (Long seatId : seatIds) {
            try {
                eventServiceClient.releaseSeat(eventId, seatId);
            } catch (Exception ignored) {
                // Best-effort rollback — if this also fails, the seat is stuck BOOKED
                // with no active booking. Worth logging loudly; a reconciliation job
                // would be the production-grade fix, out of scope for this project.
            }
        }
    }

    private BookingResponse toBookingResponse(Booking booking) {
        List<Long> seatIds = booking.getBookingSeats().stream()
                .map(BookingSeat::getSeatId)
                .collect(Collectors.toList());

        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .eventId(booking.getEventId())
                .userId(booking.getUserId())
                .status(booking.getStatus())
                .seatIds(seatIds)
                .totalAmount(booking.getTotalAmount())
                .createdAt(booking.getCreatedAt())
                .confirmedAt(booking.getConfirmedAt())
                .build();
    }
}

