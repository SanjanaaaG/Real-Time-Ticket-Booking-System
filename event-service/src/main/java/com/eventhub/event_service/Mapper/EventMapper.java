package com.eventhub.event_service.Mapper;

import com.eventhub.event_service.DTO.EventRequest;
import com.eventhub.event_service.DTO.EventResponse;
import com.eventhub.event_service.DTO.VenueRequest;
import com.eventhub.event_service.Entity.Event;
import com.eventhub.event_service.Entity.EventStatusEnum;
import com.eventhub.event_service.Entity.Venue;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventMapper {

     public Event toEventEntity(EventRequest eventRequest){
         Event e = new Event();
         e.setName(eventRequest.getName());
         e.setDescription(eventRequest.getDescription());
         e.setCategory(eventRequest.getCategory());
         e.setEventDateTime(eventRequest.getEventDateTime());
         e.setCreatedAt(LocalDateTime.now());
         return e;
     }

     public EventResponse toEventResponse(Event event){
         EventResponse er = new EventResponse();
         er.setEventId(event.getEventId());
         er.setName(event.getName());
         er.setDescription(event.getDescription());
         er.setCategory(event.getCategory());
         er.setEventDateTime(event.getEventDateTime());
         er.setCreatedAt(LocalDateTime.now());
         er.setVenue(event.getVenue());
         return er;
     }

}
