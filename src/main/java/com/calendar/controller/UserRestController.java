package com.calendar.controller;

import com.calendar.entity.User;
import com.calendar.exception.UserNotFoundException;
import com.calendar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserRestController {

    @Autowired
    UserRepository repository;

    @PostMapping("/users/sign_up")
    public User signUp(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    @GetMapping("/users/sign_in")
    public User signIn(@RequestParam String email, @RequestParam String pass) {
        return repository.findByEmailAndAndPass(email, pass)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping("/users")
    public List<User> getAll() {
        return repository.findAll();
    }

    @PutMapping("users/update/{id}")
    public User update(@PathVariable Long id, @RequestParam String pass) {

        return repository.findById(id)
                .map(employee -> {
                    employee.setPass(pass);
                    return repository.save(employee);
                })
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
