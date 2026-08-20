package com.eventhub.event_service.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Venue {
    @Id
    @GeneratedValue
    private long venueId;
    @NotNull
    private String name;
    @NotNull
    private String city;
    @NotNull
    private String address;
    private Integer capacity;
}
