package com.example.webflux_ms_capabilities.infrastructure.input.handler;

import com.example.webflux_ms_capabilities.application.dto.request.CapabilityIdsRequest;
import com.example.webflux_ms_capabilities.application.dto.request.CapabilityRequest;
import com.example.webflux_ms_capabilities.application.dto.response.CapabilityPageResponse;
import com.example.webflux_ms_capabilities.application.dto.response.CapabilityResponse;
import com.example.webflux_ms_capabilities.application.handler.ICapabilityRestHandler;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
public class CapabilityHandlerTest {

    @Mock
    private ICapabilityRestHandler capabilityRestHandler;

    @Mock
    private Validator validator;

    @InjectMocks
    private CapabilityHandler capabilityHandler;

    @Test
    public void test_valid_capability_request_returns_created_response() {
        ServerRequest request = MockServerRequest.builder()
                .body(Mono.just(new CapabilityRequest("Test Capability", "Test Description", Arrays.asList(1L, 2L, 3L))));

        Mockito.when(validator.validate(any(CapabilityRequest.class))).thenReturn(Collections.emptySet());
        Mockito.when(capabilityRestHandler.createCapability(any(CapabilityRequest.class))).thenReturn(Mono.empty());

        // Act
        Mono<ServerResponse> response = capabilityHandler.createCapability(request);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode() == HttpStatus.CREATED)
                .verifyComplete();

        Mockito.verify(capabilityRestHandler).createCapability(any(CapabilityRequest.class));
    }

    @Test
    public void test_missing_name_returns_bad_request() {
        CapabilityRequest invalidRequest = new CapabilityRequest("", "Test Description", Arrays.asList(1L, 2L, 3L));
        ServerRequest request = MockServerRequest.builder()
                .body(Mono.just(invalidRequest));

        Set<ConstraintViolation<CapabilityRequest>> violations = new HashSet<>();
        ConstraintViolation<CapabilityRequest> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);

        Mockito.when(path.toString()).thenReturn("name");
        Mockito.when(violation.getPropertyPath()).thenReturn(path);
        Mockito.when(violation.getMessage()).thenReturn("es requerido");
        violations.add(violation);

        Mockito.when(validator.validate(any(CapabilityRequest.class))).thenReturn(violations);

        // Act
        Mono<ServerResponse> response = capabilityHandler.createCapability(request);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> {
                    return serverResponse.statusCode() == HttpStatus.BAD_REQUEST &&
                            serverResponse.headers().getContentType().equals(MediaType.APPLICATION_JSON);
                })
                .verifyComplete();

        Mockito.verify(validator).validate(any(CapabilityRequest.class));
        Mockito.verify(capabilityRestHandler, Mockito.never()).createCapability(any(CapabilityRequest.class));
    }

    @Test
    public void test_get_capabilities_returns_ok_response() {
        ServerRequest request = MockServerRequest.builder().build();
        CapabilityPageResponse pageResponse = new CapabilityPageResponse();
        Mockito.when(capabilityRestHandler.getCapabilities(request)).thenReturn(Mono.just(pageResponse));

        // Act
        Mono<ServerResponse> result = capabilityHandler.getCapabilities(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    assertEquals(HttpStatus.OK, response.statusCode());
                    assertEquals(MediaType.APPLICATION_JSON, response.headers().getContentType());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void test_get_capabilities_by_ids_success() {
        List<Long> capabilityIds = Arrays.asList(1L, 2L, 3L);
        CapabilityIdsRequest request = new CapabilityIdsRequest(capabilityIds);

        List<CapabilityResponse> expectedCapabilities = Arrays.asList(
                new CapabilityResponse(),
                new CapabilityResponse()
        );

        ServerRequest serverRequest = MockServerRequest.builder()
                .body(Mono.just(request));

        Mockito.when(capabilityRestHandler.getCapabilitiesByIds(capabilityIds))
                .thenReturn(Mono.just(expectedCapabilities));

        // Act
        Mono<ServerResponse> responseMono = capabilityHandler.getCapabilitiesByIds(serverRequest);

        // Assert
        StepVerifier.create(responseMono)
                .expectNextMatches(serverResponse -> {
                    assertEquals(HttpStatus.OK, serverResponse.statusCode());
                    assertEquals(MediaType.APPLICATION_JSON, serverResponse.headers().getContentType());
                    return true;
                })
                .verifyComplete();

        Mockito.verify(capabilityRestHandler).getCapabilitiesByIds(capabilityIds);
    }
}
