package com.eventhub.event_service.Repository;

import com.eventhub.event_service.DTO.SeatResponse;
import com.eventhub.event_service.Entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepo extends JpaRepository<Seat, Integer> {
    public List<Seat> findByEvent_EventId(Long eventId);
    public Seat findByIdAndEvent_EventId(Long seatId, Long eventId);
}
