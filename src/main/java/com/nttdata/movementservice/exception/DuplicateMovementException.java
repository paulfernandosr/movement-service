package com.nttdata.movementservice.exception;

import org.springframework.http.HttpStatus;

public class DuplicateMovementException extends DomainException {

    public DuplicateMovementException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
