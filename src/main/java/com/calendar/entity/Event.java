package com.calendar.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "EVENTS")
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private Timestamp timestamp;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private boolean isPrivateEvent;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private EventType eventType;

    @Column(nullable = false)
    private String contactInfo;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @ManyToMany
    @JoinTable(
            name = "event_participants",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private Set<User> participants;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return isPrivateEvent == event.isPrivateEvent &&
                Objects.equals(id, event.id) &&
                Objects.equals(title, event.title) &&
                Objects.equals(timestamp, event.timestamp) &&
                Objects.equals(location, event.location) &&
                eventType == event.eventType &&
                Objects.equals(contactInfo, event.contactInfo) &&
                Objects.equals(description, event.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, timestamp, location, isPrivateEvent, eventType, contactInfo, description);
    }
}
