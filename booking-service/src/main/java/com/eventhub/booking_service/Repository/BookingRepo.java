package com.eventhub.booking_service.Repository;

import com.eventhub.booking_service.Entity.Booking;
import com.eventhub.booking_service.Entity.BookingStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByStatusAndCreatedAtBefore(BookingStatusEnum status, LocalDateTime cutoff);
    Optional<Booking> findById(Long id);

}