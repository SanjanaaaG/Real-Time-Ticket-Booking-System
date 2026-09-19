package com.eventhub.booking_service.ServiceImpl;

import com.eventhub.booking_service.Entity.BookingEvent;
import com.eventhub.booking_service.Service.BookingEventPublisher;
import lombok.RequiredArgsConstructor;;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingEventPublisherImpl implements BookingEventPublisher {

    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    @Value("${booking.kafka.topic}")
    private String topic;

    @Override
    public void publish(BookingEvent event) {
        // Key by bookingId so all events for the same booking land on the same
        // partition, in order — matters if a consumer ever needs event ordering.
        kafkaTemplate.send(topic, String.valueOf(event.getBookingId()), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish {} for booking {}", event.getEventType(), event.getBookingId(), ex);
                    } else {
                        log.info("Published {} for booking {}", event.getEventType(), event.getBookingId());
                    }
                });
    }
}
