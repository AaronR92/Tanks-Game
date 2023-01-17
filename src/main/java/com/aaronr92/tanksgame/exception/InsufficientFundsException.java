package com.aaronr92.tanksgame.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InsufficientFundsException extends ResponseStatusException {
    public InsufficientFundsException() {
        super(HttpStatus.BAD_REQUEST, "Insufficient funds");
    }
}
