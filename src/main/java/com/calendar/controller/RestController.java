package com.calendar.controller;

import com.calendar.entity.User;
import com.calendar.exception.UserNotFoundException;
import com.calendar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    UserRepository repository;

    @PostMapping("/users/sign_up")
    public User signUp(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    @GetMapping("/users/sign_in")
    @ResponseBody
    public User signIn(@RequestParam(name = "email") String email, @RequestParam(name = "pass") String pass) {
        return repository.findByEmailAndAndPass(email, pass)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping("/users")
    @ResponseBody
    public List<User> getAll() {
        return repository.findAll();
    }

    @PutMapping("users/update/{id}")
    @ResponseBody
    public User update(@PathVariable Long id, @RequestBody User updateUser) {

        return repository.findById(id)
                .map(employee -> {
                    employee.setEmail(updateUser.getEmail());
                    employee.setPass(updateUser.getPass());
                    return repository.save(employee);
                })
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
