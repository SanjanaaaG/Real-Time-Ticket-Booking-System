package com.eventhub.event_service.Service;

import com.eventhub.event_service.DTO.VenueRequest;
import com.eventhub.event_service.DTO.VenueResponse;
import com.eventhub.event_service.Entity.Venue;
import org.springframework.stereotype.Service;

import java.util.List;

public interface VenueService {
    public VenueResponse createVenue(VenueRequest venue);
    public List<VenueResponse> getAllVenues();
    public VenueResponse getVenueById(Long venueId);
}
