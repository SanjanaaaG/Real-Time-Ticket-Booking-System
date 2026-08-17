package com.eventhub.event_service.ServiceImpl;

import com.eventhub.event_service.DTO.EventRequest;
import com.eventhub.event_service.DTO.EventResponse;
import com.eventhub.event_service.Entity.Event;
import com.eventhub.event_service.Entity.EventStatusEnum;
import com.eventhub.event_service.Entity.Seat;
import com.eventhub.event_service.Entity.Venue;
import com.eventhub.event_service.Mapper.EventMapper;
import com.eventhub.event_service.Repository.EventRepo;
import com.eventhub.event_service.Repository.SeatRepo;
import com.eventhub.event_service.Repository.VenueRepo;
import com.eventhub.event_service.Service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventImpl implements EventService {

    private final EventRepo erepo;
    private final EventMapper emapper;
    private final VenueRepo vrepo;
    private final SeatRepo srepo;

    public EventImpl(EventRepo erepo, EventMapper emapper , VenueRepo vrepo ,  SeatRepo srepo) {
        this.erepo = erepo;
        this.emapper = emapper;
        this.vrepo = vrepo;
        this.srepo = srepo;
    }


    @Override
    public EventResponse createEvent(EventRequest eventRequest) {
        try{
            Venue v = new Venue();
            Event event = new Event();
            event = emapper.toEventEntity(eventRequest);
            v = vrepo.findById(eventRequest.getVenueId())
                    .orElseThrow(() -> new RuntimeException("Venue not found"));
            event.setVenue(v);
            event.setEventStatus(EventStatusEnum.UPCOMING);
            erepo.save(event);
            List<Seat> seats = emapper.toSeatsEntity(event,eventRequest.getSeatLayout().getRows());
            srepo.saveAll(seats);

            int totalSeats = seats.size();
            long availableSeats = seats.size();
            return emapper.toEventResponse(event,totalSeats,availableSeats);
        }catch(Exception e){
            throw new RuntimeException("Error creating event: " + e.getMessage());
        }
    }

    @Override
    public List<EventResponse> getAllEvents() {
        List<Event> events = erepo.findAll();
        return events.stream()
                .map(emapper::toEventResponse)
                .toList();
    }

    @Override
    public EventResponse getEventById(Long id) {
        Event e = erepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        return emapper.toEventResponse(e);
    }

    @Override
    public EventResponse deleteById(Long id) {
        Event e = erepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        erepo.delete(e);
        return emapper.toEventResponse(e);
    }

    @Override
    public EventResponse updateEvent(EventRequest event, Long id) {
        Event e = erepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        e.setName(event.getName());
        e.setDescription(event.getDescription());
        e.setCategory(event.getCategory());
        e.setEventDateTime(event.getEventDateTime());
        erepo.save(e);
        return emapper.toEventResponse(e);
    }

    @Override
    public EventResponse cancelEvent(Long eventId) {
        try{
            Event event = erepo.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
            event.setEventStatus(EventStatusEnum.CANCELLED);
            erepo.save(event);
            return emapper.toEventResponse(event);
        }catch(Exception e){
            throw new RuntimeException("Error cancelling event: " + e.getMessage());
        }
    }
}
