package com.nttdata.movementservice.exception;

import org.springframework.http.HttpStatus;

public class MovementNotFoundException extends DomainException {

    public MovementNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

}
