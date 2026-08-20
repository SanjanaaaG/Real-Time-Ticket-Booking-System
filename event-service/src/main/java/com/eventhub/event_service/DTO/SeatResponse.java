package com.eventhub.event_service.DTO;

import com.eventhub.event_service.Entity.SeatStatusEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SeatResponse {
    private Long Seatid;
    private String seatRow;
    private int seatNumber;
    private String seatType;
    private BigDecimal price;
    private SeatStatusEnum seatStatus;
}
