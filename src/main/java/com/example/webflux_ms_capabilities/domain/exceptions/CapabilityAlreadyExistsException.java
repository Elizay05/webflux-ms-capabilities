package com.example.webflux_ms_capabilities.domain.exceptions;

public class CapabilityAlreadyExistsException extends RuntimeException {
    public CapabilityAlreadyExistsException(String message) {
        super(message);
    }
}
