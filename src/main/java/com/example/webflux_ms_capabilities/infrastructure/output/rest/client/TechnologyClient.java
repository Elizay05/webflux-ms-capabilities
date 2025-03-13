package com.example.webflux_ms_capabilities.infrastructure.output.rest.client;

import com.example.webflux_ms_capabilities.domain.model.TechnologyModel;
import com.example.webflux_ms_capabilities.domain.spi.ITechnologyPersistencePort;
import com.example.webflux_ms_capabilities.infrastructure.output.rest.exception.TechnologyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.example.webflux_ms_capabilities.infrastructure.output.rest.util.constants.ConstantsRest.BODY_TECHNOLOGY_IDS;
import static com.example.webflux_ms_capabilities.infrastructure.output.rest.util.constants.ConstantsRest.PATH_TECHNOLOGIES_BY_IDS;

@Component
@RequiredArgsConstructor
public class TechnologyClient implements ITechnologyPersistencePort {

    private final WebClient.Builder webClientBuilder;

    @Override
    public Mono<List<TechnologyModel>> getTechnologiesByIds(List<Long> technologyIds) {
        return webClientBuilder.build()
                .post()
                .uri(PATH_TECHNOLOGIES_BY_IDS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Collections.singletonMap(BODY_TECHNOLOGY_IDS, technologyIds))
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse ->
                        clientResponse.bodyToMono(Map.class)
                                .flatMap(errorBody -> Mono.error(
                                        new TechnologyNotFoundException(errorBody.get("message").toString())
                                ))
                )
                .bodyToMono(new ParameterizedTypeReference<List<TechnologyModel>>() {});
    }
}
