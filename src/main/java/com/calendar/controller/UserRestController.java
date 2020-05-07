package com.calendar.controller;

import com.calendar.entity.Event;
import com.calendar.entity.User;
import com.calendar.exception.UserAlreadyRegistered;
import com.calendar.exception.UserNotFoundException;
import com.calendar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class UserRestController {

    @Autowired
    UserRepository repository;

    @PostMapping("/api/users/sign_up")
    public ResponseEntity<?> signUp(@RequestBody User newUser) {
        String mail = newUser.getEmail();
        repository.findByEmail(mail).ifPresent(user -> {
            throw new UserAlreadyRegistered(user.getEmail());
        });

        repository.save(newUser);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/api/users/sign_in")
    public User signIn(@RequestBody User user) {
        String mail = user.getEmail();
        return repository.findByEmail(mail)
                .map(registered_user -> {
                        if (registered_user.getPass().equals(user.getPass())) {
                            return registered_user;
                        } else {
                          throw new UserNotFoundException(mail);
                        }
                })
                .orElseThrow(UserNotFoundException::new);
    }

    @PutMapping("/api/users/edit")
    public ResponseEntity<User> editUser(@RequestBody User user) {
        long userId = user.getId();
        User userState = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return new ResponseEntity<>(userSetParams(userState, user), HttpStatus.OK);
    }

    private User userSetParams(User userState, User user) {
        userState.setFirst_name(user.getFirst_name());
        userState.setLast_name(user.getLast_name());
        userState.setPosition(user.getPosition());
        userState.setCity(user.getCity());
        userState.setPhone(user.getPhone());
        String pass = user.getPass();
        if (pass != null) {
            userState.setPass(user.getPass());
        }

        return repository.save(userState);
    }

    @GetMapping("/api/users/{id}")
    public User getUser(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping("/api/users")
    public List<User> getAll() {
        return repository.findAll();
    }

    @PutMapping("/api/users/update/{id}")
    public User update(@PathVariable Long id, @RequestParam String pass) {

        return repository.findById(id)
                .map(employee -> {
                    employee.setPass(pass);
                    return repository.save(employee);
                })
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping("/api/users/{id}/events")
    public Set<Event> getEventUsers(@PathVariable Long id) {
        return repository.findById(id)
                .map(User::getEvents)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
