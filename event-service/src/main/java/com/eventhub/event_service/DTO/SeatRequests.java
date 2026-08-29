package com.eventhub.event_service.DTO;

import com.eventhub.event_service.Entity.SeatStatusEnum;
import com.eventhub.event_service.Entity.SeatTypeEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SeatRequests {
    private String seatRow;
    private Integer seatsInRow;
    @Enumerated(EnumType.STRING)
    private SeatTypeEnum seatType;
    @Enumerated(EnumType.STRING)
    private SeatStatusEnum status;
    private BigDecimal price;
}
