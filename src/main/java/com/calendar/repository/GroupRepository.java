package com.calendar.repository;

import com.calendar.entity.Group;
import com.calendar.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByName(String name);
}
