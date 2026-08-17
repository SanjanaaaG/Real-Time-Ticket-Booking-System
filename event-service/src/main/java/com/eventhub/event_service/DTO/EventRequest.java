package com.eventhub.event_service.DTO;

import jakarta.persistence.Column;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventRequest {
    @Column(name = "venue_id")
    private Long venueId;
    private String name;
    private String description;
    private String category;
    private LocalDateTime eventDateTime;
    private SeatLayoutRequest seatLayout;
}
