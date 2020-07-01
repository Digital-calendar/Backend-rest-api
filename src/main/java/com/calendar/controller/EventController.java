package com.calendar.controller;

import com.calendar.entity.Event;
import com.calendar.entity.Group;
import com.calendar.entity.User;
import com.calendar.exception.EventNotFoundException;
import com.calendar.repository.EventRepository;
import com.calendar.repository.GroupRepository;
import com.calendar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@RestController
public class EventController {

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/api/events/create")
    public Event createEvent(@RequestBody Event event) {

        List<User> users = userRepo.findAll();
        event.getParticipants().removeIf(user -> !users.contains(user));

        return eventRepo.save(event);
    }

    @GetMapping("/api/events")
    public List<Event> getAllEvents() {
        return eventRepo.findAll();
    }

    @GetMapping("/api/events/{id}")
    public Event getEvent(@PathVariable Long id) {
        return eventRepo.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
    }

    @GetMapping("/api/events/{id}/participants")
    public Set<User> getParticipants(@PathVariable Long id) {
        return eventRepo.findById(id)
                .map(Event::getParticipants)
                .orElseThrow(() -> new EventNotFoundException(id));
    }

    @GetMapping("/api/events/{id}/groups")
    public Set<Group> getGroups(@PathVariable Long id) {
        return eventRepo.findById(id)
                .map(Event::getGroups)
                .orElseThrow(() -> new EventNotFoundException(id));
    }

    @PutMapping("/api/events/{id}/edit")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event event) {
        return eventRepo.findById(id)
                .map(currentEvent -> eventRepo.save(updateFields(event, currentEvent)))
                .orElseThrow(() -> new EventNotFoundException(id));
    }

    @PutMapping("/api/events/{id}/edit/add_user")
    public Event addUser(@PathVariable Long id, @RequestBody User user) {
        return eventRepo.findById(id)
                .map(currentEvent -> {
                    Set<User> users = currentEvent.getParticipants();
                    if (users != null) {
                        users.add(user);
                    }
                    return eventRepo.save(currentEvent);
                }).orElseThrow(() -> new EventNotFoundException(id));
    }

    @PutMapping("/api/events/{id}/edit/add_group")
    public Event addGroup(@PathVariable Long id, @RequestBody Group group) {
        return eventRepo.findById(id)
                .map(currentEvent -> {
                    Set<Group> groups = currentEvent.getGroups();
                    if (groups != null) {
                        groups.add(group);
                    }
                    return eventRepo.save(currentEvent);
                }).orElseThrow(() -> new EventNotFoundException(id));
    }

    @PutMapping("/api/events/{id}/edit/add_all")
    public Event addAll(@PathVariable Long id) {
        return eventRepo.findById(id)
                .map(currentEvent -> {
                    Set<Group> groups = currentEvent.getGroups();
                    Set<User> users = currentEvent.getParticipants();
                    if (groups != null && users != null) {
                        groups.addAll(groupRepo.findAll());
                        users.addAll(userRepo.findAll());
                    }
                    return eventRepo.save(currentEvent);
                }).orElseThrow(() -> new EventNotFoundException(id));
    }

    @DeleteMapping("/api/events/{id}/edit/delete_user/{userID}")
    public Event deleteUser(@PathVariable Long id, @PathVariable Long userID) {
        return eventRepo.findById(id)
                .map(currentEvent -> {
                    if (currentEvent.getParticipants() != null) {
                        currentEvent.getParticipants().removeIf(user -> user.getId().equals(userID));
                    }
                    return eventRepo.save(currentEvent);
                }).orElseThrow(() -> new EventNotFoundException(id));
    }

    @DeleteMapping("/api/events/{id}/edit/delete_group/{groupID}")
    public Event deleteGroup(@PathVariable Long id, @PathVariable Long groupID) {
        return eventRepo.findById(id)
                .map(currentEvent -> {
                    if (currentEvent.getGroups() != null) {
                        currentEvent.getGroups().removeIf(group -> group.getId().equals(groupID));
                    }
                    return eventRepo.save(currentEvent);
                }).orElseThrow(() -> new EventNotFoundException(id));
    }

    @DeleteMapping("/api/events/delete/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventRepo.deleteById(id);
    }

    private Event updateFields(Event event, Event currentEvent) {
        currentEvent.setTitle(event.getTitle());
//        currentEvent.setDate(event.getDate());
//        currentEvent.setTime(event.getTime());
        currentEvent.setTimestamp(event.getTimestamp());
        currentEvent.setLocation(event.getLocation());
        currentEvent.setPrivateEvent(event.isPrivateEvent());
        currentEvent.setEventType(event.getEventType());
        currentEvent.setContactInfo(event.getContactInfo());
        currentEvent.setDescription(event.getDescription());
        currentEvent.setParticipants(event.getParticipants());
        currentEvent.setGroups(event.getGroups());

        return currentEvent;
    }

}
