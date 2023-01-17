package com.aaronr92.tanksgame.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OperationNotFoundException extends ResponseStatusException {
    public OperationNotFoundException() {
        super(HttpStatus.BAD_REQUEST, "Operation not found");
    }
}
