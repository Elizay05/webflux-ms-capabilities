package com.example.webflux_ms_capabilities.infrastructure.input.handler;

import com.example.webflux_ms_capabilities.application.dto.request.CapabilityIdsRequest;
import com.example.webflux_ms_capabilities.application.dto.request.CapabilityRequest;
import com.example.webflux_ms_capabilities.application.handler.ICapabilityRestHandler;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.webflux_ms_capabilities.infrastructure.input.util.constants.ConstantsInput.ERROR;

@Component
@RequiredArgsConstructor
public class CapabilityHandler {

    private final ICapabilityRestHandler capabilityRestHandler;
    private final Validator validator;

    public Mono<ServerResponse> createCapability(ServerRequest request) {
        return request.bodyToMono(CapabilityRequest.class)
                .flatMap(capabilityRequest -> {
                    Set<ConstraintViolation<CapabilityRequest>> violations = validator.validate(capabilityRequest);
                    if (!violations.isEmpty()) {
                        String errorMessage = violations.stream()
                                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                                .collect(Collectors.joining(", "));

                        return ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue("{" + "\"" + ERROR + "\": \"" + errorMessage + "\"}");
                    }

                    return capabilityRestHandler.createCapability(capabilityRequest)
                            .then(ServerResponse.status(HttpStatus.CREATED).build());
                });
    }

    public Mono<ServerResponse> getCapabilitiesByIds(ServerRequest request) {
        return request.bodyToMono(CapabilityIdsRequest.class)
                .flatMap(capabilityIdsRequest ->
                        capabilityRestHandler.getCapabilitiesByIds(capabilityIdsRequest.getCapabilityIds())
                                .flatMap(capabilities -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(capabilities))
                );
    }
}
