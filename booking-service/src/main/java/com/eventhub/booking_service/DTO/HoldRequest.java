package com.eventhub.booking_service.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class HoldRequest {
    @NotNull(message = "eventId is required")
    private Long eventId;

    @NotNull(message = "userId is required")
    private Long userId;   // later this comes from the JWT, not the request body — see note below

    @NotEmpty(message = "At least one seatId is required")
    private List<Long> seatIds;
}
