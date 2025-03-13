package com.example.webflux_ms_capabilities.infrastructure.output.rest.exception;

public class TechnologyNotFoundException extends RuntimeException {
    public TechnologyNotFoundException(String message) {
        super(message);
    }
}
