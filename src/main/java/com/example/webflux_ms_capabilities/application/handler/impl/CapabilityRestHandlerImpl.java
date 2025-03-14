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
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.example.webflux_ms_capabilities.application.utils.constants.ConstantsApplication.*;

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
        int page = Integer.parseInt(request.queryParam(PAGE).orElse(DEFAULT_PAGE));
        int size = Integer.parseInt(request.queryParam(SIZE).orElse(DEFAULT_SIZE));
        boolean asc = Boolean.parseBoolean(request.queryParam(ASC).orElse(DEFAULT_ASC));
        String sortBy = request.queryParam(SORT_BY).orElse(DEFAULT_SORT_BY);

        return capabilityServicePort.getCapabilities(page, size, asc, sortBy)
                .map(capabilityPageResponseMapper::toCapabilityPageResponse);
    }

    @Override
    public Mono<List<CapabilityResponse>> getCapabilitiesByIds(List<Long> capabilityIds) {
        return capabilityServicePort.getCapabilitiesByIds(capabilityIds)
                .map(capabilityResponseMapper::toListCapabilityResponse);
    }
}
