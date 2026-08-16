package com.eventhub.event_service.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;
import jakarta.persistence.GenerationType;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long eventId;
    private String name;
    private String description;
    private String category;
    @ManyToOne
    private Venue venue;
    private LocalDateTime eventDateTime;
    @Enumerated(EnumType.STRING)
    private EventStatusEnum eventStatus;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL,orphanRemoval=true)
    private List<Seat> seats;
    private LocalDateTime createdAt;

}
