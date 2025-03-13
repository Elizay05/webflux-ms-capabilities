package com.example.webflux_ms_capabilities.domain.spi;

import com.example.webflux_ms_capabilities.domain.model.TechnologyModel;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyPersistencePort {
    Mono<List<TechnologyModel>> getTechnologiesByIds(List<Long> technologyIds);
}
