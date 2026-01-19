package com.reservas.reservas_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ValidatedRequestException extends RuntimeException {
    public ValidatedRequestException(String message) {
        super(message);
    }
}