package com.eventhub.booking_service.Entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue
    private Long BookingId;
    private Long userId;
    private Long eventId;
    private BookingStatusEnum status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    @Nullable
    private LocalDateTime confirmedAt;

}
