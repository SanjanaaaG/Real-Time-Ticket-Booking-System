package com.eventhub.event_service.Repository;

import com.eventhub.event_service.Entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepo extends JpaRepository<Seat, Integer> {
}
