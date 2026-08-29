package com.eventhub.event_service.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import java.math.BigDecimal;

@Entity
@Data
public class Seat {
    @Id
    @GeneratedValue
    private long seatId;
    @ManyToOne
    private Event event;
    private String seatRow;
    private Integer seatNumber;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private SeatTypeEnum seatType;

    @Enumerated(EnumType.STRING)
    private SeatStatusEnum status;

}
