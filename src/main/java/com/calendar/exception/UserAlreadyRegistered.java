package com.calendar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.IM_USED)
public class UserAlreadyRegistered extends RuntimeException{
    public UserAlreadyRegistered(String email) {
        super("User " + email + " have been registered");
    }
}
