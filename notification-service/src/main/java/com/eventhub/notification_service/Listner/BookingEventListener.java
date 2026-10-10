package com.eventhub.notification_service.Listner;


import com.eventhub.notification_service.Event.BookingEvent;
import com.eventhub.notification_service.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingEventListener {

    private final NotificationService notificationService;

    @KafkaListener(topics = "${booking.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleBookingEvent(BookingEvent event) {
        switch (event.getEventType()) {
            case "BOOKING_CONFIRMED" -> notificationService.sendBookingConfirmation(event);
            case "BOOKING_EXPIRED" -> notificationService.sendBookingExpiredNotice(event);
            default -> notificationService.sendGenericNotice(event);
        }
    }
}