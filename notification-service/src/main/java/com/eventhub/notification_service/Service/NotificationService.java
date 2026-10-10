package com.eventhub.notification_service.Service;


import com.eventhub.notification_service.Event.BookingEvent;

public interface NotificationService {
    void sendBookingConfirmation(BookingEvent event);
    void sendBookingExpiredNotice(BookingEvent event);
    void sendGenericNotice(BookingEvent event);
}