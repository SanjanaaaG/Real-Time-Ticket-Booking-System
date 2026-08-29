package com.eventhub.event_service.Entity;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum SeatTypeEnum{
    @Enumerated(EnumType.STRING)
    VIP,
    REGULAR,
    PREMIUM
}
