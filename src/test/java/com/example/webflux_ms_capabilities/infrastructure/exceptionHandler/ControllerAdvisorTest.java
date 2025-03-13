package com.example.webflux_ms_capabilities.infrastructure.exceptionHandler;

import com.example.webflux_ms_capabilities.domain.exceptions.CapabilityAlreadyExistsException;
import com.example.webflux_ms_capabilities.domain.exceptions.DuplicateTechnologiesException;
import com.example.webflux_ms_capabilities.domain.exceptions.MaxTechnologiesCapabilityException;
import com.example.webflux_ms_capabilities.domain.exceptions.MinTechnologiesCapabilityException;
import com.example.webflux_ms_capabilities.infrastructure.output.mysql.exceptions.CapabilityNotFoundException;
import com.example.webflux_ms_capabilities.infrastructure.output.rest.exception.TechnologyNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ControllerAdvisorTest {

    @InjectMocks
    private ControllerAdvisor controllerAdvisor;

    @Test
    public void testCapabilityAlreadyExistsException() {
        String errorMessage = "Capability already exists";
        CapabilityAlreadyExistsException exception = new CapabilityAlreadyExistsException(errorMessage);

        ResponseEntity<ExceptionResponse> responseEntity = controllerAdvisor.handleCapabilityAlreadyExistsException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ExceptionResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(errorMessage, response.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.toString(), response.getStatus());
        assertNotNull(response.getTimestamp());
    }

    @Test
    public void testDuplicateTechnologiesException() {
        String errorMessage = "Duplicate technologies";
        DuplicateTechnologiesException exception = new DuplicateTechnologiesException(errorMessage);

        ResponseEntity<ExceptionResponse> responseEntity = controllerAdvisor.handleDuplicateTechnologiesException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ExceptionResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(errorMessage, response.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.toString(), response.getStatus());
        assertNotNull(response.getTimestamp());
    }

    @Test
    public void testMaxTechnologiesCapabilityException() {
        String errorMessage = "Max technologies";
        MaxTechnologiesCapabilityException exception = new MaxTechnologiesCapabilityException(errorMessage);

        ResponseEntity<ExceptionResponse> responseEntity = controllerAdvisor.handleMaxTechnologiesCapabilityException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ExceptionResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(errorMessage, response.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.toString(), response.getStatus());
        assertNotNull(response.getTimestamp());
    }

    @Test
    public void testMinTechnologiesCapabilityException() {
        String errorMessage = "Min technologies";
        MinTechnologiesCapabilityException exception = new MinTechnologiesCapabilityException(errorMessage);

        ResponseEntity<ExceptionResponse> responseEntity = controllerAdvisor.handleMinTechnologiesCapabilityException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ExceptionResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(errorMessage, response.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.toString(), response.getStatus());
        assertNotNull(response.getTimestamp());
    }

    @Test
    public void testTechnologyNotFoundException() {
        String errorMessage = "Technology not found";
        TechnologyNotFoundException exception = new TechnologyNotFoundException(errorMessage);

        ResponseEntity<ExceptionResponse> responseEntity = controllerAdvisor.handleTechnologyNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        ExceptionResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(errorMessage, response.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.toString(), response.getStatus());
        assertNotNull(response.getTimestamp());
    }

    @Test
    public void testCapabilityNotFoundException() {
        String errorMessage = "Capability not found";
        CapabilityNotFoundException exception = new CapabilityNotFoundException(errorMessage);

        ResponseEntity<ExceptionResponse> responseEntity = controllerAdvisor.handleCapabilityNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        ExceptionResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(errorMessage, response.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.toString(), response.getStatus());
        assertNotNull(response.getTimestamp());
    }

}
