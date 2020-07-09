package com.calendar.controller;

import com.calendar.entity.Event;
import com.calendar.entity.Group;
import com.calendar.entity.User;
import com.calendar.exception.EventNotFoundException;
import com.calendar.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class GroupsController {

    @Autowired
    GroupRepository groupRepository;

    @GetMapping("/api/groups")
    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    @GetMapping("/api/groups/{id}/users")
    public Set<User> getUsers(@PathVariable Long id) {
        return groupRepository.findById(id)
                .map(Group::getUsers)
                .orElseThrow(() -> new EventNotFoundException(id));
    }
}
