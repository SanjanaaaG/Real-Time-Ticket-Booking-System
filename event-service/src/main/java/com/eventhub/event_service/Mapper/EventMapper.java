package com.eventhub.event_service.Mapper;

import com.eventhub.event_service.DTO.EventRequest;
import com.eventhub.event_service.DTO.EventResponse;
import com.eventhub.event_service.DTO.SeatRequests;
import com.eventhub.event_service.Entity.Event;
import com.eventhub.event_service.Entity.Seat;
import com.eventhub.event_service.Entity.SeatStatusEnum;
import com.eventhub.event_service.Entity.SeatTypeEnum;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class EventMapper {

    public List<Seat> toSeatsEntity(Event event , List<SeatRequests> seatRequests){
        List<Seat> seats = new ArrayList<>();
        for(SeatRequests seatRequest : seatRequests){
            for(int i=1 ; i<=seatRequest.getSeatsInRow() ; i++){
                Seat seat = new Seat();
                seat.setEvent(event);
                seat.setSeatRow(seatRequest.getRowLabel());
                seat.setSeatNumber(i);
                seat.setSeatType(SeatTypeEnum.valueOf(seatRequest.getSeatType()));
                seat.setPrice(seatRequest.getPrice());
                seat.setStatus(SeatStatusEnum.AVAILABLE);
                seats.add(seat);
            }
        }
        return seats;
    }

     public Event toEventEntity(EventRequest eventRequest){
         Event e = new Event();
         e.setName(eventRequest.getName());
         e.setDescription(eventRequest.getDescription());
         e.setCategory(eventRequest.getCategory());
         e.setEventDateTime(eventRequest.getEventDateTime());
         e.setCreatedAt(LocalDateTime.now());
         return e;
     }

     public EventResponse toEventResponse(Event event,int totalSeats, long availableSeats){
         EventResponse er = new EventResponse();
         er.setEventId(event.getEventId());
         er.setName(event.getName());
         er.setDescription(event.getDescription());
         er.setCategory(event.getCategory());
         er.setEventDateTime(event.getEventDateTime());
         er.setCreatedAt(LocalDateTime.now());
         er.setVenue(event.getVenue());
         er.setTotalSeats(totalSeats);
         er.setAvailableSeats(availableSeats);
         return er;
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
