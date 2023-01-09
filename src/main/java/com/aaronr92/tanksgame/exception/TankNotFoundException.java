package com.aaronr92.tanksgame.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TankNotFoundException extends ResponseStatusException {
    public TankNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Tank not found");
    }
}
