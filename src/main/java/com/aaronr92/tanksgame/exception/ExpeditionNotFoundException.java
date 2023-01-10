package com.aaronr92.tanksgame.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ExpeditionNotFoundException extends ResponseStatusException {
    public ExpeditionNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Expedition not found");
    }
}
