package com.example.webflux_ms_capabilities.infrastructure.output.mysql.repository;

import com.example.webflux_ms_capabilities.infrastructure.output.mysql.entity.CapabilityTechnologyEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CapabilityTechnologyRepository extends R2dbcRepository<CapabilityTechnologyEntity, Long> {
    @Query("SELECT technology_id FROM capability_technology WHERE capability_id = :capabilityId")
    Flux<Long> findTechnologyIdsByCapabilityId(@Param("capabilityId") Long capabilityId);

    Flux<CapabilityTechnologyEntity> findByCapabilityId(Long capabilityId);

    @Query("SELECT COUNT(*) FROM capability_technology WHERE capability_id = :capabilityId")
    Mono<Integer> countByCapabilityId(@Param("capabilityId") Long capabilityId);
}
