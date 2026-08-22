package com.eventhub.event_service.Controller;

import com.eventhub.event_service.DTO.SeatResponse;
import com.eventhub.event_service.ServiceImpl.SeatImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Seat {

    @Autowired
    private SeatImpl seatImpl;

    @GetMapping("/api/v1/seats")
    public ResponseEntity<List<SeatResponse>> getAllSeats() {
        List<SeatResponse> s = seatImpl.findAll();
        try{
            if(s != null){
                return ResponseEntity.ok().body(s);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/api/v1/events/{eventId}/seats")
    public ResponseEntity<List<SeatResponse>> getAllSeatsByEventId(@PathVariable Long eventId) {
        List<SeatResponse> s = seatImpl.findSeatByEventId(eventId);
        try{
            if(s != null){
                return ResponseEntity.ok().body(s);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/api/v1/events/{eventId}/seats/{seatId}/book")
    public ResponseEntity<List<SeatResponse>> seatBooked(@PathVariable Long eventId, @PathVariable Long seatId){
        List<SeatResponse> s = seatImpl.updateSeatBooked(eventId,seatId);
        try{
            if(s != null){
                return new ResponseEntity<>(s, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }

}
