package com.eventhub.booking_service.Service;

import com.eventhub.booking_service.Entity.BookingEvent;

public interface BookingEventPublisher {
    void publish(BookingEvent event);
}