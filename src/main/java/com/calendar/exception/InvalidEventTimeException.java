package com.calendar.exception;

public class InvalidEventTimeException extends RuntimeException {

    public InvalidEventTimeException(Long idForChecking, Long id) {
        super("Event is in conflict with other event " + id);
    }

}