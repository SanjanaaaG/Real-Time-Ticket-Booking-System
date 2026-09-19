package com.eventhub.booking_service.Schedular;


import com.eventhub.booking_service.Entity.Booking;
import com.eventhub.booking_service.Entity.BookingEvent;
import com.eventhub.booking_service.Entity.BookingSeat;
import com.eventhub.booking_service.Entity.BookingStatusEnum;
import com.eventhub.booking_service.Repository.BookingRepo;
import com.eventhub.booking_service.Service.BookingEventPublisher;
import com.eventhub.booking_service.Service.SeatHoldService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingExpiryScheduler {

    private final BookingRepo bookingRepository;
    private final SeatHoldService seatHoldService;
    private final BookingEventPublisher bookingEventPublisher;

    @Value("${booking.hold.duration-seconds}")
    private long holdDurationSeconds;

    // Runs every booking.expiry.scan-interval-ms (default 30s). A poller is simpler
    // and easier to reason about than relying on Redis keyspace-notification events,
    // at the cost of up to one scan-interval of delay before a stale HELD booking
    // gets cleaned up — an acceptable tradeoff for this use case.
    @Scheduled(fixedRateString = "${booking.expiry.scan-interval-ms}")
    @Transactional
    public void expireStaleHolds() {
        LocalDateTime cutoff = LocalDateTime.now().minusSeconds(holdDurationSeconds);
        List<Booking> staleBookings = bookingRepository.findByStatusAndCreatedAtBefore(
                BookingStatusEnum.HELD, cutoff);

        if (staleBookings.isEmpty()) {
            return;
        }

        log.info("Found {} stale HELD bookings to expire", staleBookings.size());

        for (Booking booking : staleBookings) {
            List<Long> seatIds = booking.getBookingSeats().stream()
                    .map(BookingSeat::getSeatId)
                    .collect(Collectors.toList());

            // Redis TTL has almost certainly already deleted these keys by now,
            // but call releaseHold anyway — it's a safe no-op if the key is
            // already gone, and cheap insurance if this scheduler run is early.
            for (Long seatId : seatIds) {
                seatHoldService.releaseHold(booking.getEventId(), seatId);
            }

            booking.setStatus(BookingStatusEnum.EXPIRED);
            bookingRepository.save(booking);

            bookingEventPublisher.publish(BookingEvent.builder()
                    .eventType("BOOKING_EXPIRED")
                    .bookingId(booking.getBookingId())
                    .eventId(booking.getEventId())
                    .userId(booking.getUserId())
                    .seatIds(seatIds)
                    .status(booking.getStatus())
                    .totalAmount(booking.getTotalAmount())
                    .timestamp(LocalDateTime.now())
                    .build());

            log.info("Expired booking {} — seats {} released", booking.getBookingId(), seatIds);
        }
    }
}