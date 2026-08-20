package com.eventhub.event_service.Mapper;

import com.eventhub.event_service.DTO.SeatRequests;
import com.eventhub.event_service.DTO.SeatResponse;
import com.eventhub.event_service.Entity.Seat;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper {

    public SeatResponse toSeatResponse(Seat seat){
        SeatResponse seatResponse = new SeatResponse();
        seatResponse.setSeatid(seat.getSeatId());
        seatResponse.setSeatRow(seat.getSeatRow());
        seatResponse.setSeatNumber(seat.getSeatNumber());
        seatResponse.setPrice(seat.getPrice());
        seatResponse.setSeatStatus(seat.getStatus());
        seatResponse.setSeatType(seat.getSeatType().name());
        return seatResponse;


    }
}
