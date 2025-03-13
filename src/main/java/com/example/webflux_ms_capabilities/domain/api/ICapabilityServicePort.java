package com.example.webflux_ms_capabilities.domain.api;

import com.example.webflux_ms_capabilities.domain.model.CapabilityModel;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICapabilityServicePort {
    Mono<Void> createCapability(CapabilityModel capabilityModel);
    Mono<List<CapabilityModel>> getCapabilitiesByIds(List<Long> capabilityIds);
}
