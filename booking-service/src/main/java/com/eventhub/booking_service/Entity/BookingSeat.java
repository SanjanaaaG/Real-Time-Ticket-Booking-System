package com.eventhub.booking_service.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class BookingSeat {
    @Id
    @GeneratedValue
    private Long BookingSeatId;
    @ManyToOne
    private Booking Booking;
    private Long seatId;
    private BigDecimal price;
}
