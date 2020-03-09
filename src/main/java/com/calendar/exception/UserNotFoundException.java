package com.calendar.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id) {
        super("Could not find user " + id);
    }

    public UserNotFoundException(String email) {
        super("Could not find user " + email);
    }

}
