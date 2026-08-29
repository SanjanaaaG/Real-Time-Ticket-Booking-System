package com.eventhub.booking_service.Service;

import com.eventhub.booking_service.DTO.HoldRequest;
import com.eventhub.booking_service.DTO.HoldResponse;

public interface BookingService {
    HoldResponse holdSeats(HoldRequest request);
}
