package com.eventhub.event_service.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SeatRequests {
    private String rowLabel;
    private Integer seatsInRow;
    private String seatType;
    private BigDecimal price;
}
