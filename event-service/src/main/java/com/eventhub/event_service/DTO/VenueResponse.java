package com.eventhub.event_service.DTO;

import lombok.Data;

@Data
public class VenueResponse {
    private String name;
    private String city;
    private String address;
    private Integer capacity;
}
