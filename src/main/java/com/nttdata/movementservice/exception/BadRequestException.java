package com.nttdata.movementservice.exception;

import com.nttdata.movementservice.dto.ValidationDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class BadRequestException extends DomainException {

    private final List<ValidationDto> validations;
    private final String requestClass;

    public BadRequestException(String message, String requestClass, List<ValidationDto> validations) {
        super(HttpStatus.BAD_REQUEST, message);
        this.validations = validations;
        this.requestClass = requestClass;
    }

}
