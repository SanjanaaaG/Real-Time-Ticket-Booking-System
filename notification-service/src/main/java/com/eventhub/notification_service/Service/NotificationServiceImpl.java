package com.eventhub.notification_service.Service;

import com.eventhub.notification_service.Event.BookingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    // Mocked as logs for the portfolio project — swapping this for a real email/SMS
    // provider (SendGrid, Twilio) later would only change this class, nothing else.

    @Override
    public void sendBookingConfirmation(BookingEvent event) {
        log.info("✅ [EMAIL] Booking {} confirmed for user {} — seats {} — total ₹{}",
                event.getBookingId(), event.getUserId(), event.getSeatIds(), event.getTotalAmount());
    }

    @Override
    public void sendBookingExpiredNotice(BookingEvent event) {
        log.info("⌛ [EMAIL] Booking {} expired for user {} — seats {} released",
                event.getBookingId(), event.getUserId(), event.getSeatIds());
    }

    @Override
    public void sendGenericNotice(BookingEvent event) {
        log.info("ℹ️ [EMAIL] Unhandled event type {} for booking {}",
                event.getEventType(), event.getBookingId());
    }
}