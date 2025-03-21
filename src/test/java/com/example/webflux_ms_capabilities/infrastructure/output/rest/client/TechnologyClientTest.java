package com.example.webflux_ms_capabilities.infrastructure.output.rest.client;

import com.example.webflux_ms_capabilities.domain.model.TechnologyModel;
import com.example.webflux_ms_capabilities.infrastructure.output.rest.exception.TechnologyNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.example.webflux_ms_capabilities.infrastructure.output.rest.util.constants.ConstantsRest.BODY_TECHNOLOGY_IDS;
import static com.example.webflux_ms_capabilities.infrastructure.output.rest.util.constants.ConstantsRest.PATH_TECHNOLOGIES_BY_IDS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TechnologyClientTest {

    @Test
    public void test_successfully_retrieves_technologies_when_valid_ids_provided() {
        // Arrange
        WebClient.Builder webClientBuilderMock = mock(WebClient.Builder.class);
        WebClient webClientMock = mock(WebClient.class);
        WebClient.RequestBodyUriSpec requestBodyUriSpecMock = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec requestBodySpecMock = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        List<Long> technologyIds = Arrays.asList(1L, 2L);
        List<TechnologyModel> expectedTechnologies = Arrays.asList(
                new TechnologyModel(1L, "Java", "Programming language"),
                new TechnologyModel(2L, "Spring", "Framework")
        );

        when(webClientBuilderMock.build()).thenReturn(webClientMock);
        when(webClientMock.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri(PATH_TECHNOLOGIES_BY_IDS)).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.bodyValue(Collections.singletonMap(BODY_TECHNOLOGY_IDS, technologyIds))).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.onStatus(any(), any())).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(expectedTechnologies));

        TechnologyClient technologyClient = new TechnologyClient(webClientBuilderMock);

        // Act
        Mono<List<TechnologyModel>> result = technologyClient.getTechnologiesByIds(technologyIds);

        // Assert
        StepVerifier.create(result)
                .expectNext(expectedTechnologies)
                .verifyComplete();
    }

    @Test
    public void test_throws_technology_not_found_exception_on_404() {
        // Arrange
        WebClient.Builder webClientBuilderMock = mock(WebClient.Builder.class);
        WebClient webClientMock = mock(WebClient.class);
        WebClient.RequestBodyUriSpec requestBodyUriSpecMock = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec requestBodySpecMock = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        List<Long> technologyIds = Arrays.asList(1L, 2L);
        Map<String, String> errorResponse = Collections.singletonMap("message", "Technology not found");

        when(webClientBuilderMock.build()).thenReturn(webClientMock);
        when(webClientMock.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri(PATH_TECHNOLOGIES_BY_IDS)).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.bodyValue(Collections.singletonMap(BODY_TECHNOLOGY_IDS, technologyIds)))
                .thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);

        when(responseSpecMock.onStatus(any(), any())).thenAnswer(invocation -> {
            Function<ClientResponse, Mono<? extends Throwable>> errorHandler = invocation.getArgument(1);
            return responseSpecMock;
        });

        when(responseSpecMock.bodyToMono(new ParameterizedTypeReference<List<TechnologyModel>>() {}))
                .thenReturn(Mono.error(new TechnologyNotFoundException(errorResponse.get("message"))));

        TechnologyClient technologyClient = new TechnologyClient(webClientBuilderMock);

        // Act & Assert
        StepVerifier.create(technologyClient.getTechnologiesByIds(technologyIds))
                .expectErrorMatches(throwable -> throwable instanceof TechnologyNotFoundException &&
                        throwable.getMessage().equals("Technology not found"))
                .verify();
    }
}
