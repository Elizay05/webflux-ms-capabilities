package com.example.webflux_ms_capabilities.infrastructure.output.mysql.exceptions;

public class CapabilityNotFoundException extends RuntimeException {
    public CapabilityNotFoundException(String message) {
        super(message);
    }
}
