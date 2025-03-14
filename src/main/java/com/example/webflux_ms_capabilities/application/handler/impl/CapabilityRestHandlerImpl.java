package com.example.webflux_ms_capabilities.application.handler.impl;

import com.example.webflux_ms_capabilities.application.dto.request.CapabilityRequest;
import com.example.webflux_ms_capabilities.application.dto.response.CapabilityPageResponse;
import com.example.webflux_ms_capabilities.application.dto.response.CapabilityResponse;
import com.example.webflux_ms_capabilities.application.handler.ICapabilityRestHandler;
import com.example.webflux_ms_capabilities.application.mapper.ICapabilityPageResponseMapper;
import com.example.webflux_ms_capabilities.application.mapper.ICapabilityRequestMapper;
import com.example.webflux_ms_capabilities.application.mapper.ICapabilityResponseMapper;
import com.example.webflux_ms_capabilities.domain.api.ICapabilityServicePort;
import com.example.webflux_ms_capabilities.domain.model.CapabilityModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class CapabilityRestHandlerImpl implements ICapabilityRestHandler {
    private final ICapabilityServicePort capabilityServicePort;
    private final ICapabilityRequestMapper capabilityRequestMapper;
    private final ICapabilityResponseMapper capabilityResponseMapper;
    private final ICapabilityPageResponseMapper capabilityPageResponseMapper;

    @Override
    public Mono<Void> createCapability(CapabilityRequest capabilityRequest) {
        CapabilityModel capabilityModel = capabilityRequestMapper.toCapabilityModel(capabilityRequest);
        return capabilityServicePort.createCapability(capabilityModel);
    }

    @Override
    public Mono<CapabilityPageResponse> getCapabilities(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        boolean asc = Boolean.parseBoolean(request.queryParam("asc").orElse("true"));
        String sortBy = request.queryParam("sortBy").orElse("name");

        return capabilityServicePort.getCapabilities(page, size, asc, sortBy)
                .map(capabilityPageResponseMapper::toCapabilityPageResponse);
    }

    @Override
    public Mono<List<CapabilityResponse>> getCapabilitiesByIds(List<Long> capabilityIds) {
        return capabilityServicePort.getCapabilitiesByIds(capabilityIds)
                .map(capabilityResponseMapper::toListCapabilityResponse);
    }
}
