package com.eventhub.event_service.Service;

import com.eventhub.event_service.DTO.EventRequest;
import com.eventhub.event_service.DTO.EventResponse;
import com.eventhub.event_service.Entity.Event;

import java.util.List;

public interface EventService {
    public EventResponse createEvent(EventRequest event);
    public List<EventResponse> getAllEvents();
    public EventResponse getEventById(Long id);
    public EventResponse deleteById(Long id);
    public EventResponse updateEvent(EventRequest event , Long id);
    public EventResponse cancelEvent(Long id);
}
