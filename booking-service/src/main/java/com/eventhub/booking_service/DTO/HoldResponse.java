package com.eventhub.booking_service.DTO;

import com.eventhub.booking_service.Entity.BookingStatusEnum;
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
public class HoldResponse {
    private Long bookingId;
    private Long eventId;
    private BookingStatusEnum status;
    private List<Long> seatIds;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime holdExpiresAt;
}
