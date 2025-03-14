package com.example.webflux_ms_capabilities.infrastructure.configuration;

import com.example.webflux_ms_capabilities.application.handler.ICapabilityRestHandler;
import com.example.webflux_ms_capabilities.application.handler.impl.CapabilityRestHandlerImpl;
import com.example.webflux_ms_capabilities.application.mapper.ICapabilityRequestMapper;
import com.example.webflux_ms_capabilities.application.mapper.ICapabilityResponseMapper;
import com.example.webflux_ms_capabilities.domain.api.ICapabilityServicePort;
import com.example.webflux_ms_capabilities.domain.spi.ICapabilityPersistencePort;
import com.example.webflux_ms_capabilities.domain.spi.ITechnologyPersistencePort;
import com.example.webflux_ms_capabilities.domain.usecase.CapabilityUseCase;
import com.example.webflux_ms_capabilities.infrastructure.output.mysql.adapter.CapabilityAdapter;
import com.example.webflux_ms_capabilities.infrastructure.output.mysql.mapper.ICapabilityEntityMapper;
import com.example.webflux_ms_capabilities.infrastructure.output.mysql.repository.CapabilityTechnologyRepository;
import com.example.webflux_ms_capabilities.infrastructure.output.mysql.repository.ICapabilityRepository;
import com.example.webflux_ms_capabilities.infrastructure.output.rest.client.TechnologyClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {
    private final ICapabilityRepository capabilityRepository;
    private final CapabilityTechnologyRepository capabilityTechnologyRepository;
    private final ICapabilityEntityMapper capabilityEntityMapper;
    private final ICapabilityRequestMapper capabilityRequestMapper;
    private final ICapabilityResponseMapper capabilityResponseMapper;

    @Bean
    public ICapabilityServicePort capabilityServicePort(ICapabilityPersistencePort capabilityPersistencePort,
                                                        ITechnologyPersistencePort technologyPersistencePort) {
        return new CapabilityUseCase(capabilityPersistencePort, technologyPersistencePort);
    }

    @Bean
    public ITechnologyPersistencePort technologyPersistencePort(WebClient.Builder webClientBuilder) {
        return new TechnologyClient(webClientBuilder);
    }

    @Bean
    public ICapabilityPersistencePort capabilityPersistencePort() {
        return new CapabilityAdapter(capabilityRepository, capabilityTechnologyRepository, capabilityEntityMapper);
    }

    @Bean
    public ICapabilityRestHandler capabilityRestHandler(ICapabilityServicePort capabilityServicePort) {
        return new CapabilityRestHandlerImpl(capabilityServicePort, capabilityRequestMapper, capabilityResponseMapper);
    }
}
