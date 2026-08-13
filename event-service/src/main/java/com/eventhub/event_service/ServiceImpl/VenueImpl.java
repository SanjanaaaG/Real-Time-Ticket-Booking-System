package com.eventhub.event_service.ServiceImpl;

import com.eventhub.event_service.DTO.VenueRequest;
import com.eventhub.event_service.DTO.VenueResponse;
import com.eventhub.event_service.Entity.Venue;
import com.eventhub.event_service.Mapper.VenueMapper;
import com.eventhub.event_service.Repository.VenueRepo;
import com.eventhub.event_service.Service.VenueService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenueImpl implements VenueService {

    private final VenueRepo vrepo;
    private final VenueMapper vmapper;

    public VenueImpl(VenueRepo vrepo, VenueMapper vmapper){
        this.vrepo = vrepo;
        this.vmapper = vmapper;
    }

    @Override
    public VenueResponse createVenue(VenueRequest venue) {
        Venue v = vmapper.toVenueEntity(venue);
        Venue savedVenue = vrepo.save(v);
        return vmapper.toVenueResponse(savedVenue);
    }

    @Override
    public List<VenueResponse> getAllVenues() {
        List<Venue> venues = vrepo.findAll();
        return venues.stream()
                .map(vmapper::toVenueResponse)
                .toList();
    }

    @Override
    public VenueResponse getVenueById(Long venueId) {
        Venue venue = vrepo.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found with id: " + venueId));
        return vmapper.toVenueResponse(venue);
    }
}
