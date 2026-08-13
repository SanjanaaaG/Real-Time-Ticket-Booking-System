package com.eventhub.event_service.DTO;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class VenueRequest {
    private String name;
    private String city;
    private String address;
    private Integer capacity;
}
