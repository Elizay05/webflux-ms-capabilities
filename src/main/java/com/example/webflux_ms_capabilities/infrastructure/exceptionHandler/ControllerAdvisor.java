package com.example.webflux_ms_capabilities.infrastructure.exceptionHandler;

import com.example.webflux_ms_capabilities.domain.exceptions.CapabilityAlreadyExistsException;
import com.example.webflux_ms_capabilities.domain.exceptions.DuplicateTechnologiesException;
import com.example.webflux_ms_capabilities.domain.exceptions.MaxTechnologiesCapabilityException;
import com.example.webflux_ms_capabilities.domain.exceptions.MinTechnologiesCapabilityException;
import com.example.webflux_ms_capabilities.infrastructure.output.mysql.exceptions.CapabilityNotFoundException;
import com.example.webflux_ms_capabilities.infrastructure.output.rest.exception.TechnologyNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerAdvisor {
    @ExceptionHandler(CapabilityAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleCapabilityAlreadyExistsException(CapabilityAlreadyExistsException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
    @ExceptionHandler(DuplicateTechnologiesException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateTechnologiesException(DuplicateTechnologiesException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
    @ExceptionHandler(MaxTechnologiesCapabilityException.class)
    public ResponseEntity<ExceptionResponse> handleMaxTechnologiesCapabilityException(MaxTechnologiesCapabilityException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
    @ExceptionHandler(MinTechnologiesCapabilityException.class)
    public ResponseEntity<ExceptionResponse> handleMinTechnologiesCapabilityException(MinTechnologiesCapabilityException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
    @ExceptionHandler(TechnologyNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleTechnologyNotFoundException(TechnologyNotFoundException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND.toString(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }
    @ExceptionHandler(CapabilityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleCapabilityNotFoundException(CapabilityNotFoundException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND.toString(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }
}
