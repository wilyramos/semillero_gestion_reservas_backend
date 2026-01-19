package com.reservas.reservas_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class HandleJwtException extends RuntimeException {

    public HandleJwtException(String message) {
        super(message);
    }    

}