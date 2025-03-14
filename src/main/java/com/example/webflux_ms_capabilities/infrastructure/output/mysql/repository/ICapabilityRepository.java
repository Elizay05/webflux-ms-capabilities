package com.example.webflux_ms_capabilities.infrastructure.output.mysql.repository;

import com.example.webflux_ms_capabilities.infrastructure.output.mysql.entity.CapabilityEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface ICapabilityRepository extends R2dbcRepository<CapabilityEntity, Long> {
    Mono<Boolean> existsCapabilityEntitiesByName(String name);
}
