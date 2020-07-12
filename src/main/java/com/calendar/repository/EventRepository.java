package com.calendar.repository;

import com.calendar.entity.Event;
import com.calendar.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByDeadlineEventId(long id);
}
