package com.eventhub.event_service.ServiceImpl;

import com.eventhub.event_service.DTO.SeatRequests;
import com.eventhub.event_service.DTO.SeatResponse;
import com.eventhub.event_service.Entity.Seat;
import com.eventhub.event_service.Entity.SeatStatusEnum;
import com.eventhub.event_service.Mapper.SeatMapper;
import com.eventhub.event_service.Repository.EventRepo;
import com.eventhub.event_service.Repository.SeatRepo;
import com.eventhub.event_service.Service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatImpl implements SeatService {

    private final SeatRepo seatRepo;
    private final SeatMapper seatMapper;

    public SeatImpl(SeatRepo seatRepo, SeatMapper seatMapper) {
        this.seatRepo = seatRepo;
        this.seatMapper = seatMapper;
    }

    @Override
    public List<SeatResponse> findAll() {
        List<Seat> seats = seatRepo.findAll();
        return seats.stream()
                .map(seatMapper::toSeatResponse)
                .toList();
    }

    @Override
    public List<SeatResponse> findSeatByEventId(Long eventId) {
        return seatRepo.findByEvent_EventId(eventId)
                .stream()
                .map(seat -> seatMapper.toSeatResponse(seat))
                .collect(Collectors.toList());
    }

    @Override
    public SeatResponse markSeatAsBooked(Long eventId, Long seatId) {
        Seat seats = seatRepo.findByIdAndEvent_EventId(seatId,eventId);
        if(seats.getStatus() == SeatStatusEnum.BOOKED){
            throw new RuntimeException("Seat is already booked");
        }
        seats.setStatus(SeatStatusEnum.BOOKED);
        seatRepo.save(seats);
        return seatMapper.toSeatResponse(seats);
    }

    @Override
    public SeatResponse releaseSeat(Long eventId, Long seatId) {
        Seat seats = seatRepo.findByIdAndEvent_EventId(seatId,eventId);
        if(seats.getStatus() == SeatStatusEnum.AVAILABLE){
            throw new RuntimeException("Seat is already available");
        }
        seats.setStatus(SeatStatusEnum.AVAILABLE);
        seatRepo.save(seats);
        return seatMapper.toSeatResponse(seats);
    }

}
