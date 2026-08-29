package com.eventhub.booking_service.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Builder
public class BookingSeat {
    @Id
    @GeneratedValue
    private Long BookingSeatId;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    private Long seatId;
    private BigDecimal price;

}
