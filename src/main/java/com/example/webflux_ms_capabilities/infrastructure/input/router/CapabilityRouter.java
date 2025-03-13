package com.example.webflux_ms_capabilities.infrastructure.input.router;

import com.example.webflux_ms_capabilities.infrastructure.input.handler.CapabilityHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.example.webflux_ms_capabilities.infrastructure.input.util.constants.ConstantsInput.PATH_CAPABILITIES;
import static com.example.webflux_ms_capabilities.infrastructure.input.util.constants.ConstantsInput.PATH_CAPABILITIES_BY_IDS;

@Configuration
public class CapabilityRouter {

    @Bean
    public RouterFunction<ServerResponse> capabilityRoutes(CapabilityHandler handler) {
        return RouterFunctions.route()
                .POST(PATH_CAPABILITIES, handler::createCapability)
                .POST(PATH_CAPABILITIES_BY_IDS, handler::getCapabilitiesByIds)
                .build();
    }
}
