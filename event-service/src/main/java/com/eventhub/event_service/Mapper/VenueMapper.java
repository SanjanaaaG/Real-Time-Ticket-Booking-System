package com.eventhub.event_service.Mapper;

import com.eventhub.event_service.DTO.VenueRequest;
import com.eventhub.event_service.DTO.VenueResponse;
import com.eventhub.event_service.Entity.Venue;
import org.springframework.stereotype.Component;

@Component
public class VenueMapper {

    public Venue toVenueEntity(VenueRequest venueRequest){
        Venue v = new Venue();
        v.setName(venueRequest.getName());
        v.setCity(venueRequest.getCity());
        v.setAddress(venueRequest.getAddress());
        v.setCapacity(venueRequest.getCapacity());
        return v;
    }

    public VenueResponse toVenueResponse(Venue venue){
        VenueResponse v = new VenueResponse();
        v.setName(venue.getName());
        v.setCity(venue.getCity());
        v.setAddress(venue.getAddress());
        v.setCapacity(venue.getCapacity());
        return v;
    }
}
