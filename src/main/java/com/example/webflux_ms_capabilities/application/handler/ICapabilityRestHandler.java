package com.example.webflux_ms_capabilities.application.handler;

import com.example.webflux_ms_capabilities.application.dto.request.CapabilityRequest;
import com.example.webflux_ms_capabilities.application.dto.response.CapabilityPageResponse;
import com.example.webflux_ms_capabilities.application.dto.response.CapabilityResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICapabilityRestHandler {
    Mono<Void> createCapability(CapabilityRequest capabilityRequest);
    Mono<CapabilityPageResponse> getCapabilities(ServerRequest request);
    Mono<List<CapabilityResponse>> getCapabilitiesByIds(List<Long> capabilityIds);
}
