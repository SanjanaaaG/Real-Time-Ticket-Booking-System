package com.eventhub.event_service.Service;

import com.eventhub.event_service.DTO.SeatResponse;
import com.eventhub.event_service.Entity.Seat;

import java.util.List;

public interface SeatService {
    public List<SeatResponse> findAll();
    public List<SeatResponse> findSeatByEventId(Long eventId);
    public SeatResponse markSeatAsBooked(Long eventId, Long seatId);
    public SeatResponse releaseSeat(Long eventId, Long seatId);
}
