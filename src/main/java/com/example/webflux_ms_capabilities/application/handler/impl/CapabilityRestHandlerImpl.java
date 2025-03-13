package com.example.webflux_ms_capabilities.application.handler.impl;

import com.example.webflux_ms_capabilities.application.dto.request.CapabilityRequest;
import com.example.webflux_ms_capabilities.application.dto.response.CapabilityResponse;
import com.example.webflux_ms_capabilities.application.handler.ICapabilityRestHandler;
import com.example.webflux_ms_capabilities.application.mapper.ICapabilityRequestMapper;
import com.example.webflux_ms_capabilities.application.mapper.ICapabilityResponseMapper;
import com.example.webflux_ms_capabilities.domain.api.ICapabilityServicePort;
import com.example.webflux_ms_capabilities.domain.model.CapabilityModel;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class CapabilityRestHandlerImpl implements ICapabilityRestHandler {
    private final ICapabilityServicePort capabilityServicePort;
    private final ICapabilityRequestMapper capabilityRequestMapper;
    private final ICapabilityResponseMapper capabilityResponseMapper;

    @Override
    public Mono<Void> createCapability(CapabilityRequest capabilityRequest) {
        CapabilityModel capabilityModel = capabilityRequestMapper.toCapabilityModel(capabilityRequest);
        return capabilityServicePort.createCapability(capabilityModel);
    }

    @Override
    public Mono<List<CapabilityResponse>> getCapabilitiesByIds(List<Long> capabilityIds) {
        return capabilityServicePort.getCapabilitiesByIds(capabilityIds)
                .map(capabilityResponseMapper::toListCapabilityResponse);
    }
}
