package com.example.webflux_ms_capabilities.infrastructure.output.mysql.adapter;

import com.example.webflux_ms_capabilities.domain.model.CapabilityModel;
import com.example.webflux_ms_capabilities.domain.model.CapabilityPageModel;
import com.example.webflux_ms_capabilities.domain.model.TechnologyModel;
import com.example.webflux_ms_capabilities.infrastructure.output.mysql.entity.CapabilityEntity;
import com.example.webflux_ms_capabilities.infrastructure.output.mysql.entity.CapabilityTechnologyEntity;
import com.example.webflux_ms_capabilities.infrastructure.output.mysql.exceptions.CapabilityNotFoundException;
import com.example.webflux_ms_capabilities.infrastructure.output.mysql.mapper.ICapabilityEntityMapper;
import com.example.webflux_ms_capabilities.infrastructure.output.mysql.repository.CapabilityTechnologyRepository;
import com.example.webflux_ms_capabilities.infrastructure.output.mysql.repository.ICapabilityRepository;
import com.example.webflux_ms_capabilities.infrastructure.output.mysql.util.constants.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.webflux_ms_capabilities.infrastructure.output.mysql.util.constants.Constants.SOME_CAPABILITIES_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CapabilityAdapterTest {

    @Mock
    private ICapabilityRepository capabilityRepository;

    @Mock
    private CapabilityTechnologyRepository capabilityTechnologyRepository;

    @Mock
    private ICapabilityEntityMapper capabilityEntityMapper;

    @InjectMocks
    private CapabilityAdapter capabilityAdapter;

    @Test
    public void test_save_capability_with_technologies_successfully() {
        CapabilityModel capabilityModel = new CapabilityModel(null, "Test Capability", "Test Description",
                List.of(new TechnologyModel(1L, "Tech1", "Tech Description")));

        CapabilityEntity capabilityEntity = new CapabilityEntity(null, "Test Capability", "Test Description");
        CapabilityEntity savedCapabilityEntity = new CapabilityEntity(1L, "Test Capability", "Test Description");

        CapabilityTechnologyEntity relation = new CapabilityTechnologyEntity(1L, 1L);

        when(capabilityEntityMapper.toEntity(capabilityModel)).thenReturn(capabilityEntity);
        when(capabilityRepository.save(capabilityEntity)).thenReturn(Mono.just(savedCapabilityEntity));
        when(capabilityTechnologyRepository.saveAll(List.of(relation))).thenReturn(Flux.just(relation));

        // Act
        Mono<Void> result = capabilityAdapter.saveCapability(capabilityModel);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        Mockito.verify(capabilityEntityMapper).toEntity(capabilityModel);
        Mockito.verify(capabilityRepository).save(capabilityEntity);
        Mockito.verify(capabilityTechnologyRepository).saveAll(List.of(relation));
    }

    @Test
    public void test_returns_true_when_capability_exists() {
        String capabilityName = "Java";

        when(capabilityRepository.existsCapabilityEntitiesByName(capabilityName))
                .thenReturn(Mono.just(true));

        // Act
        Mono<Boolean> result = capabilityAdapter.existCapabilityByName(capabilityName);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        Mockito.verify(capabilityRepository).existsCapabilityEntitiesByName(capabilityName);
    }

    @Test
    public void test_get_all_capabilities_with_default_sorting() {
        // Arrange
        CapabilityEntity entity1 = new CapabilityEntity(1L, "Capability B", "Description B");
        CapabilityEntity entity2 = new CapabilityEntity(2L, "Capability A", "Description A");

        CapabilityModel model1 = new CapabilityModel(1L, "Capability B", "Description B", new ArrayList<>());
        CapabilityModel model2 = new CapabilityModel(2L, "Capability A", "Description A", new ArrayList<>());

        when(capabilityRepository.findAll()).thenReturn(Flux.just(entity1, entity2));
        when(capabilityTechnologyRepository.findByCapabilityId(1L)).thenReturn(Flux.empty());
        when(capabilityTechnologyRepository.findByCapabilityId(2L)).thenReturn(Flux.empty());
        when(capabilityEntityMapper.toModel(entity1)).thenReturn(model1);
        when(capabilityEntityMapper.toModel(entity2)).thenReturn(model2);

        // Act
        Mono<CapabilityPageModel> result = capabilityAdapter.getAllCapabilities(0, 10, true, "name");

        // Assert
        StepVerifier.create(result)
                .assertNext(page -> {
                    assertEquals(2, page.getTotalElements());
                    assertEquals(1, page.getTotalPages());
                    assertEquals(2, page.getCapabilities().size());
                    assertEquals("Capability A", page.getCapabilities().get(0).getName());
                    assertEquals("Capability B", page.getCapabilities().get(1).getName());
                })
                .verifyComplete();
    }

    @Test
    public void test_get_all_capabilities_sorted_by_technology_count_ascending() {
        // Arrange
        CapabilityEntity entity1 = new CapabilityEntity(1L, "Capability A", "Description A");
        CapabilityEntity entity2 = new CapabilityEntity(2L, "Capability B", "Description B");

        TechnologyModel tech1 = new TechnologyModel(1L, "Tech 1", "Description 1");
        TechnologyModel tech2 = new TechnologyModel(2L, "Tech 2", "Description 2");

        CapabilityModel model1 = new CapabilityModel(1L, "Capability A", "Description A", Arrays.asList(tech1));
        CapabilityModel model2 = new CapabilityModel(2L, "Capability B", "Description B", Arrays.asList(tech1, tech2));

        when(capabilityRepository.findAll()).thenReturn(Flux.just(entity1, entity2));
        when(capabilityTechnologyRepository.findByCapabilityId(1L)).thenReturn(Flux.just(new CapabilityTechnologyEntity(1L, 1L)));
        when(capabilityTechnologyRepository.findByCapabilityId(2L)).thenReturn(Flux.just(new CapabilityTechnologyEntity(2L, 1L), new CapabilityTechnologyEntity(2L, 2L)));
        when(capabilityEntityMapper.toModel(entity1)).thenReturn(model1);
        when(capabilityEntityMapper.toModel(entity2)).thenReturn(model2);

        // Act
        Mono<CapabilityPageModel> result = capabilityAdapter.getAllCapabilities(0, 10, true, "technologyCount");

        // Assert
        StepVerifier.create(result)
                .assertNext(page -> {
                    assertEquals(2, page.getTotalElements());
                    assertEquals(1, page.getTotalPages());
                    assertEquals(2, page.getCapabilities().size());
                    assertEquals("Capability A", page.getCapabilities().get(0).getName());
                    assertEquals("Capability B", page.getCapabilities().get(1).getName());
                })
                .verifyComplete();
    }

    @Test
    public void test_get_all_capabilities_sorted_by_name_descending() {
        CapabilityAdapter capabilityAdapter = new CapabilityAdapter(capabilityRepository, capabilityTechnologyRepository, capabilityEntityMapper);

        CapabilityEntity entity1 = new CapabilityEntity(1L, "Capability A", "Description A");
        CapabilityEntity entity2 = new CapabilityEntity(2L, "Capability B", "Description B");

        CapabilityModel model1 = new CapabilityModel(1L, "Capability A", "Description A", new ArrayList<>());
        CapabilityModel model2 = new CapabilityModel(2L, "Capability B", "Description B", new ArrayList<>());

        when(capabilityRepository.findAll()).thenReturn(Flux.just(entity1, entity2));
        when(capabilityTechnologyRepository.findByCapabilityId(1L)).thenReturn(Flux.empty());
        when(capabilityTechnologyRepository.findByCapabilityId(2L)).thenReturn(Flux.empty());
        when(capabilityEntityMapper.toModel(entity1)).thenReturn(model1);
        when(capabilityEntityMapper.toModel(entity2)).thenReturn(model2);

        // Act
        Mono<CapabilityPageModel> result = capabilityAdapter.getAllCapabilities(0, 10, false, "name");

        // Assert
        StepVerifier.create(result)
                .assertNext(page -> {
                    assertEquals(2, page.getTotalElements());
                    assertEquals(1, page.getTotalPages());
                    assertEquals(2, page.getCapabilities().size());
                    assertEquals("Capability B", page.getCapabilities().get(0).getName());
                    assertEquals("Capability A", page.getCapabilities().get(1).getName());
                })
                .verifyComplete();
    }

    @Test
    public void test_get_all_capabilities_sorted_by_technology_count_desc() {
        // Arrange
        CapabilityEntity entity1 = new CapabilityEntity(1L, "Capability A", "Description A");
        CapabilityEntity entity2 = new CapabilityEntity(2L, "Capability B", "Description B");

        TechnologyModel tech1 = new TechnologyModel(1L, "Tech 1", "Description 1");
        TechnologyModel tech2 = new TechnologyModel(2L, "Tech 2", "Description 2");

        CapabilityModel model1 = new CapabilityModel(1L, "Capability A", "Description A", Arrays.asList(tech1, tech2));
        CapabilityModel model2 = new CapabilityModel(2L, "Capability B", "Description B", Collections.singletonList(tech1));

        when(capabilityRepository.findAll()).thenReturn(Flux.just(entity1, entity2));
        when(capabilityTechnologyRepository.findByCapabilityId(1L)).thenReturn(Flux.just(new CapabilityTechnologyEntity(1L, 1L), new CapabilityTechnologyEntity(1L, 2L)));
        when(capabilityTechnologyRepository.findByCapabilityId(2L)).thenReturn(Flux.just(new CapabilityTechnologyEntity(2L, 1L)));
        when(capabilityEntityMapper.toModel(entity1)).thenReturn(model1);
        when(capabilityEntityMapper.toModel(entity2)).thenReturn(model2);

        // Act
        Mono<CapabilityPageModel> result = capabilityAdapter.getAllCapabilities(0, 10, false, Constants.TECHNOLOGY_COUNT);

        // Assert
        StepVerifier.create(result)
                .assertNext(page -> {
                    assertEquals(2, page.getTotalElements());
                    assertEquals(1, page.getTotalPages());
                    assertEquals(2, page.getCapabilities().size());
                    assertEquals("Capability A", page.getCapabilities().get(0).getName());
                    assertEquals("Capability B", page.getCapabilities().get(1).getName());
                })
                .verifyComplete();
    }

    @Test
    public void test_empty_repository_returns_empty_page() {
        when(capabilityRepository.findAll()).thenReturn(Flux.empty());

        // Act
        Mono<CapabilityPageModel> result = capabilityAdapter.getAllCapabilities(0, 10, true, "name");

        // Assert
        StepVerifier.create(result)
                .assertNext(page -> {
                    assertEquals(0, page.getTotalElements());
                    assertEquals(0, page.getTotalPages());
                    assertTrue(page.getCapabilities().isEmpty());
                })
                .verifyComplete();
    }

    @Test
    public void test_get_capabilities_by_ids_success() {
        List<Long> capabilityIds = List.of(1L, 2L);

        CapabilityEntity entity1 = new CapabilityEntity(1L, "Capability 1", "Description 1");
        CapabilityEntity entity2 = new CapabilityEntity(2L, "Capability 2", "Description 2");

        CapabilityTechnologyEntity techRelation1 = new CapabilityTechnologyEntity(1L, 101L);
        CapabilityTechnologyEntity techRelation2 = new CapabilityTechnologyEntity(2L, 102L);

        CapabilityModel model1 = new CapabilityModel(1L, "Capability 1", "Description 1", new ArrayList<>());
        CapabilityModel model2 = new CapabilityModel(2L, "Capability 2", "Description 2", new ArrayList<>());

        when(capabilityRepository.findAllById(capabilityIds)).thenReturn(Flux.just(entity1, entity2));
        when(capabilityTechnologyRepository.findByCapabilityId(1L)).thenReturn(Flux.just(techRelation1));
        when(capabilityTechnologyRepository.findByCapabilityId(2L)).thenReturn(Flux.just(techRelation2));
        when(capabilityEntityMapper.toModel(entity1)).thenReturn(model1);
        when(capabilityEntityMapper.toModel(entity2)).thenReturn(model2);

        // Act
        Mono<List<CapabilityModel>> result = capabilityAdapter.getCapabilitiesByIds(capabilityIds);

        // Assert
        StepVerifier.create(result)
                .assertNext(capabilities -> {
                    assertEquals(2, capabilities.size());
                    assertEquals(1L, capabilities.get(0).getId());
                    assertEquals(2L, capabilities.get(1).getId());
                    assertEquals(1, capabilities.get(0).getTechnologies().size());
                    assertEquals(1, capabilities.get(1).getTechnologies().size());
                    assertEquals(101L, capabilities.get(0).getTechnologies().get(0).getId());
                    assertEquals(102L, capabilities.get(1).getTechnologies().get(0).getId());
                })
                .verifyComplete();
    }

    @Test
    public void test_get_capabilities_by_ids_not_found() {
        CapabilityAdapter capabilityAdapter = new CapabilityAdapter(capabilityRepository, capabilityTechnologyRepository, capabilityEntityMapper);

        List<Long> capabilityIds = List.of(1L, 2L, 3L);

        CapabilityEntity entity1 = new CapabilityEntity(1L, "Capability 1", "Description 1");
        CapabilityEntity entity2 = new CapabilityEntity(2L, "Capability 2", "Description 2");

        when(capabilityRepository.findAllById(capabilityIds)).thenReturn(Flux.just(entity1, entity2));

        // Act
        Mono<List<CapabilityModel>> result = capabilityAdapter.getCapabilitiesByIds(capabilityIds);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof CapabilityNotFoundException &&
                                throwable.getMessage().equals(SOME_CAPABILITIES_NOT_FOUND))
                .verify();
    }
}
