package com.eventhub.event_service.Entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


public enum SeatStatusEnum {
    @Enumerated(EnumType.STRING)
    AVAILABLE,
    BOOKED
}
