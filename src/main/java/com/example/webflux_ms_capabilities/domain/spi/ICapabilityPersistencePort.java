package com.example.webflux_ms_capabilities.domain.spi;

import com.example.webflux_ms_capabilities.domain.model.CapabilityModel;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICapabilityPersistencePort {
    Mono<Void> saveCapability(CapabilityModel capabilityModel);
    Mono<Boolean> existCapabilityByName(String capabilityName);
    Mono<List<CapabilityModel>> getCapabilitiesByIds(List<Long> capabilityIds);
}
