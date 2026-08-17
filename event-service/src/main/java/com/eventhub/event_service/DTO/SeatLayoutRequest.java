package com.eventhub.event_service.DTO;

import lombok.Data;

import java.util.List;

@Data
public class SeatLayoutRequest {
    List<SeatRequests> rows;
}
