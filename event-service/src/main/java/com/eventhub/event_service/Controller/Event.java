package com.eventhub.event_service.Controller;

import com.eventhub.event_service.DTO.EventRequest;
import com.eventhub.event_service.DTO.EventResponse;
import com.eventhub.event_service.Service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Event {

    @Autowired
    private EventService eventService;

    @PostMapping("/api/v1/events")
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest eventRequest){
        try{
            EventResponse e = eventService.createEvent(eventRequest);
            if(e != null){
                return new ResponseEntity<>(e , HttpStatus.CREATED);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/api/v1/events")
    public ResponseEntity<List<EventResponse>> getAllEvents(){
        try{
            List<EventResponse> e = eventService.getAllEvents();
            if(e != null){
                return new ResponseEntity<>(e , HttpStatus.OK);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/api/v1/events/{eventId}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long eventId){
        try{
            EventResponse e = eventService.getEventById(eventId);
            if(e != null){
                return new ResponseEntity<>(e , HttpStatus.OK);
            }
        }catch(Exception e){
            e.printStackTrace();
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/api/v1/events/{eventId}")
    public ResponseEntity<EventResponse> deleteEventById(@PathVariable Long eventId){
        try{
            EventResponse e = eventService.deleteById(eventId);
            if(e != null){
                return new ResponseEntity<>(e , HttpStatus.OK);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/api/v1/events/all/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(@RequestBody EventRequest eventRequest, @PathVariable Long eventId){
        try{
            EventResponse e = eventService.updateEvent(eventRequest,eventId);
            if(e != null){
                return new ResponseEntity<>(e , HttpStatus.OK);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/api/v1/events/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long eventId){
    try{
        EventResponse e = eventService.cancelEvent(eventId);
            if(e != null){
                return new ResponseEntity<>(e , HttpStatus.OK);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
