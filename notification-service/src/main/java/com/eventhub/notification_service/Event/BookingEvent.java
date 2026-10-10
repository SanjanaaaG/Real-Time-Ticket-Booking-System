package com.eventhub.notification_service.Event;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingEvent {
    private String eventType;
    private Long bookingId;
    private Long eventId;
    private Long userId;
    private List<Long> seatIds;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime timestamp;
}