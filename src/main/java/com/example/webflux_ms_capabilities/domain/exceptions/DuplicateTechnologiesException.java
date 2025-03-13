package com.example.webflux_ms_capabilities.domain.exceptions;

public class DuplicateTechnologiesException extends RuntimeException {
    public DuplicateTechnologiesException(String message) {
        super(message);
    }
}
