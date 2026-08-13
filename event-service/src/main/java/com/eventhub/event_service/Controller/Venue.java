package com.eventhub.event_service.Controller;

import com.eventhub.event_service.DTO.VenueRequest;
import com.eventhub.event_service.DTO.VenueResponse;
import com.eventhub.event_service.Service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Venue {

    @Autowired
    private VenueService venueService;

    @PostMapping("/api/v1/venues")
    public ResponseEntity<VenueResponse> createVenue(@RequestBody VenueRequest venueRequest){
        try{
            VenueResponse v = venueService.createVenue(venueRequest);
            if(v!= null){
                return new ResponseEntity<>(v,HttpStatus.CREATED);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/api/v1/venues")
    public ResponseEntity<VenueResponse> getAllVenues(){
        try{
            List<VenueResponse> v = venueService.getAllVenues();
            if(v!= null){
                return new ResponseEntity(venueService.getAllVenues(),HttpStatus.OK);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/api/v1/venues/{venueId}")
    public ResponseEntity<VenueResponse> getVenueById(@PathVariable Long venueId){
        try{
            VenueResponse v = venueService.getVenueById(venueId);
            if(v!=null){
                return new ResponseEntity<>(v,HttpStatus.OK);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
