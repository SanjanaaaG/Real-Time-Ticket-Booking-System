package com.eventhub.event_service.Repository;

import com.eventhub.event_service.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepo extends JpaRepository<Event,Long> {

}
