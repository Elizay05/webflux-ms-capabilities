package com.example.webflux_ms_capabilities.domain;

import com.example.webflux_ms_capabilities.domain.exceptions.CapabilityAlreadyExistsException;
import com.example.webflux_ms_capabilities.domain.exceptions.DuplicateTechnologiesException;
import com.example.webflux_ms_capabilities.domain.exceptions.MaxTechnologiesCapabilityException;
import com.example.webflux_ms_capabilities.domain.exceptions.MinTechnologiesCapabilityException;
import com.example.webflux_ms_capabilities.domain.model.CapabilityModel;
import com.example.webflux_ms_capabilities.domain.model.CapabilityPageModel;
import com.example.webflux_ms_capabilities.domain.model.TechnologyModel;
import com.example.webflux_ms_capabilities.domain.spi.ICapabilityPersistencePort;
import com.example.webflux_ms_capabilities.domain.spi.ITechnologyPersistencePort;
import com.example.webflux_ms_capabilities.domain.usecase.CapabilityUseCase;
import com.example.webflux_ms_capabilities.domain.utils.constants.ConstantsDomain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
public class CapabilityUseCaseTest {

    @Mock
    private ICapabilityPersistencePort capabilityPersistencePort;

    @Mock
    private ITechnologyPersistencePort technologyPersistencePort;

    @InjectMocks
    private CapabilityUseCase capabilityUseCase;

    @Test
    public void test_create_capability_with_valid_technologies() {
        // Arrange
        List<TechnologyModel> technologies = Arrays.asList(
                new TechnologyModel(1L, "Java", "Programming Language"),
                new TechnologyModel(2L, "Spring", "Framework"),
                new TechnologyModel(3L, "Reactor", "Reactive Library")
        );

        CapabilityModel capabilityModel = new CapabilityModel(null, "Backend", "Backend Development", technologies);

        Mockito.when(technologyPersistencePort.getTechnologiesByIds(Mockito.anyList())).thenReturn(Mono.just(technologies));
        Mockito.when(capabilityPersistencePort.existCapabilityByName("Backend")).thenReturn(Mono.just(false));
        Mockito.when(capabilityPersistencePort.saveCapability(capabilityModel)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = capabilityUseCase.createCapability(capabilityModel);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(capabilityPersistencePort).saveCapability(capabilityModel);
    }

    @Test
    public void test_create_capability_with_insufficient_technologies() {
        List<TechnologyModel> technologies = Arrays.asList(
                new TechnologyModel(1L, "Java", "Programming Language"),
                new TechnologyModel(2L, "Spring", "Framework")
        );

        CapabilityModel capabilityModel = new CapabilityModel(null, "Backend", "Backend Development", technologies);

        Mockito.when(technologyPersistencePort.getTechnologiesByIds(Mockito.anyList()))
                .thenReturn(Mono.just(Collections.emptyList()));

        // Act & Assert
        StepVerifier.create(capabilityUseCase.createCapability(capabilityModel))
                .expectErrorMatches(throwable ->
                        throwable instanceof MinTechnologiesCapabilityException &&
                                throwable.getMessage().equals(ConstantsDomain.MIN_TECHNOLOGIES_CAPABILITY_ERROR))
                .verify();

        Mockito.verifyNoInteractions(capabilityPersistencePort);
    }

    @Test
    public void test_create_capability_with_more_than_max_technologies() {
        List<TechnologyModel> technologies = new ArrayList<>();
        for (long i = 1; i <= 21; i++) {
            technologies.add(new TechnologyModel(i, "Tech" + i, "Description" + i));
        }

        CapabilityModel capabilityModel = new CapabilityModel(null, "Overloaded", "Too many technologies", technologies);

        Mockito.when(technologyPersistencePort.getTechnologiesByIds(Mockito.anyList()))
                .thenReturn(Mono.just(Collections.emptyList()));

        // Act & Assert
        StepVerifier.create(capabilityUseCase.createCapability(capabilityModel))
                .expectError(MaxTechnologiesCapabilityException.class)
                .verify();
    }

    @Test
    public void test_create_capability_with_duplicate_technologies() {
        List<TechnologyModel> technologies = Arrays.asList(
                new TechnologyModel(1L, "Java", "Programming Language"),
                new TechnologyModel(2L, "Spring", "Framework"),
                new TechnologyModel(1L, "Java", "Programming Language")
        );

        CapabilityModel capabilityModel = new CapabilityModel(null, "Backend", "Backend Development", technologies);

        Mockito.when(technologyPersistencePort.getTechnologiesByIds(Mockito.anyList()))
                .thenReturn(Mono.just(Collections.emptyList()));

        // Act & Assert
        StepVerifier.create(capabilityUseCase.createCapability(capabilityModel))
                .expectErrorMatches(throwable -> throwable instanceof DuplicateTechnologiesException &&
                        throwable.getMessage().equals(ConstantsDomain.DUPLICATE_TECHNOLOGIES_CAPABILITY_ERROR))
                .verify();
    }

    @Test
    public void test_create_capability_throws_exception_when_name_exists() {
        List<TechnologyModel> technologies = Arrays.asList(
                new TechnologyModel(1L, "Java", "Programming Language"),
                new TechnologyModel(2L, "Spring", "Framework"),
                new TechnologyModel(3L, "Reactor", "Reactive Library")
        );

        CapabilityModel capabilityModel = new CapabilityModel(null, "Backend", "Backend Development", technologies);

        Mockito.when(technologyPersistencePort.getTechnologiesByIds(Mockito.anyList())).thenReturn(Mono.just(technologies));
        Mockito.when(capabilityPersistencePort.existCapabilityByName("Backend")).thenReturn(Mono.just(true));

        // Act
        Mono<Void> result = capabilityUseCase.createCapability(capabilityModel);

        // Assert
        StepVerifier.create(result)
                .expectError(CapabilityAlreadyExistsException.class)
                .verify();

        Mockito.verify(capabilityPersistencePort, Mockito.never()).saveCapability(capabilityModel);
    }

    @Test
    public void test_get_capabilities_with_technologies_success() {
        int page = 0;
        int size = 10;
        boolean asc = true;
        String sortBy = "name";

        // Create test data
        TechnologyModel tech1 = new TechnologyModel(1L, "Java", "Java description");
        TechnologyModel tech2 = new TechnologyModel(2L, "Spring", "Spring description");

        // Initial tech objects with only IDs
        TechnologyModel techWithIdOnly1 = new TechnologyModel(1L, null, null);
        TechnologyModel techWithIdOnly2 = new TechnologyModel(2L, null, null);

        List<CapabilityModel> capabilities = List.of(
                new CapabilityModel(1L, "Backend", "Backend description", List.of(techWithIdOnly1)),
                new CapabilityModel(2L, "Frontend", "Frontend description", List.of(techWithIdOnly2))
        );

        CapabilityPageModel pageModel = new CapabilityPageModel(capabilities, 1, 2);

        // Mock persistence port responses
        Mockito.when(capabilityPersistencePort.getAllCapabilities(page, size, asc, sortBy))
                .thenReturn(Mono.just(pageModel));

        Mockito.when(technologyPersistencePort.getTechnologiesByIds(List.of(1L, 2L)))
                .thenReturn(Mono.just(List.of(tech1, tech2)));

        // Act
        Mono<CapabilityPageModel> result = capabilityUseCase.getCapabilities(page, size, asc, sortBy);

        // Assert
        StepVerifier.create(result)
                .assertNext(resultPage -> {
                    assertEquals(2, resultPage.getCapabilities().size());
                    assertEquals(1, resultPage.getTotalPages());
                    assertEquals(2, resultPage.getTotalElements());

                    // Verify first capability
                    CapabilityModel firstCapability = resultPage.getCapabilities().get(0);
                    assertEquals(1L, firstCapability.getId());
                    assertEquals("Backend", firstCapability.getName());
                    assertEquals(1, firstCapability.getTechnologies().size());
                    assertEquals("Java", firstCapability.getTechnologies().get(0).getName());

                    // Verify second capability
                    CapabilityModel secondCapability = resultPage.getCapabilities().get(1);
                    assertEquals(2L, secondCapability.getId());
                    assertEquals("Frontend", secondCapability.getName());
                    assertEquals(1, secondCapability.getTechnologies().size());
                    assertEquals("Spring", secondCapability.getTechnologies().get(0).getName());
                })
                .verifyComplete();
    }

    @Test
    public void test_get_capabilities_with_empty_list() {
        int page = 0;
        int size = 10;
        boolean asc = true;
        String sortBy = "name";

        // Create empty page model
        List<CapabilityModel> emptyCapabilities = List.of();
        CapabilityPageModel emptyPageModel = new CapabilityPageModel(emptyCapabilities, 0, 0);

        // Mock persistence port response
        Mockito.when(capabilityPersistencePort.getAllCapabilities(page, size, asc, sortBy))
                .thenReturn(Mono.just(emptyPageModel));

        // Act
        Mono<CapabilityPageModel> result = capabilityUseCase.getCapabilities(page, size, asc, sortBy);

        // Assert
        StepVerifier.create(result)
                .assertNext(resultPage -> {
                    assertTrue(resultPage.getCapabilities().isEmpty());
                    assertEquals(0, resultPage.getTotalPages());
                    assertEquals(0, resultPage.getTotalElements());
                })
                .verifyComplete();

        // Verify that getTechnologiesByIds was never called
        Mockito.verify(technologyPersistencePort, Mockito.never()).getTechnologiesByIds(Mockito.anyList());
    }

    @Test
    public void test_successfully_retrieves_capabilities_and_enriches_with_technology_details() {
        List<Long> capabilityIds = List.of(1L, 2L);

        TechnologyModel techWithBasicInfo1 = new TechnologyModel(101L, null, null);
        TechnologyModel techWithBasicInfo2 = new TechnologyModel(102L, null, null);

        CapabilityModel capability1 = new CapabilityModel(1L, "Capability1", "Description1",
                List.of(techWithBasicInfo1));
        CapabilityModel capability2 = new CapabilityModel(2L, "Capability2", "Description2",
                List.of(techWithBasicInfo2));

        List<CapabilityModel> capabilities = List.of(capability1, capability2);

        TechnologyModel techWithFullInfo1 = new TechnologyModel(101L, "Tech1", "TechDesc1");
        TechnologyModel techWithFullInfo2 = new TechnologyModel(102L, "Tech2", "TechDesc2");

        List<TechnologyModel> technologies = List.of(techWithFullInfo1, techWithFullInfo2);

        Mockito.when(capabilityPersistencePort.getCapabilitiesByIds(capabilityIds))
                .thenReturn(Mono.just(capabilities));
        Mockito.when(technologyPersistencePort.getTechnologiesByIds(List.of(101L, 102L)))
                .thenReturn(Mono.just(technologies));

        // Act
        Mono<List<CapabilityModel>> result = capabilityUseCase.getCapabilitiesByIds(capabilityIds);

        // Assert
        StepVerifier.create(result)
                .assertNext(enrichedCapabilities -> {
                    assertEquals(2, enrichedCapabilities.size());

                    CapabilityModel enrichedCapability1 = enrichedCapabilities.get(0);
                    assertEquals(1L, enrichedCapability1.getId());
                    assertEquals("Capability1", enrichedCapability1.getName());
                    assertEquals("Description1", enrichedCapability1.getDescription());
                    assertEquals(1, enrichedCapability1.getTechnologies().size());
                    assertEquals("Tech1", enrichedCapability1.getTechnologies().get(0).getName());
                    assertEquals("TechDesc1", enrichedCapability1.getTechnologies().get(0).getDescription());

                    CapabilityModel enrichedCapability2 = enrichedCapabilities.get(1);
                    assertEquals(2L, enrichedCapability2.getId());
                    assertEquals("Capability2", enrichedCapability2.getName());
                    assertEquals("Description2", enrichedCapability2.getDescription());
                    assertEquals(1, enrichedCapability2.getTechnologies().size());
                    assertEquals("Tech2", enrichedCapability2.getTechnologies().get(0).getName());
                    assertEquals("TechDesc2", enrichedCapability2.getTechnologies().get(0).getDescription());
                })
                .verifyComplete();
    }
}
