package com.eventhub.event_service.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Event {
    @Id
    @GeneratedValue
    @UniqueElements
    private long eventId;
    private String name;
    private String description;
    private String category;
    @ManyToOne
    private Venue venue;
    private LocalDateTime eventDateTime;
    private EventStatusEnum eventStatus;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL,orphanRemoval=true)
    private List<Seat> seats;
    private LocalDateTime createdAt;

}
