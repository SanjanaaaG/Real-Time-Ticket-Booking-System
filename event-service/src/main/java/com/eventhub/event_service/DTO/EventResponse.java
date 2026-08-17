package com.eventhub.event_service.DTO;

import com.eventhub.event_service.Entity.EventStatusEnum;
import com.eventhub.event_service.Entity.Venue;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private Long eventId;
    private String name;
    private String description;
    private String category;
    @ManyToOne
    private Venue venue;
    private LocalDateTime eventDateTime;
    private EventStatusEnum eventStatus;
    private LocalDateTime createdAt;
    private int totalSeats;
    private long availableSeats;

}
