package com.example.webflux_ms_capabilities.infrastructure.output.mysql.adapter;

import com.example.webflux_ms_capabilities.domain.model.CapabilityModel;
import com.example.webflux_ms_capabilities.domain.model.TechnologyModel;
import com.example.webflux_ms_capabilities.domain.spi.ICapabilityPersistencePort;
import com.example.webflux_ms_capabilities.infrastructure.output.mysql.entity.CapabilityEntity;
import com.example.webflux_ms_capabilities.infrastructure.output.mysql.entity.CapabilityTechnologyEntity;
import com.example.webflux_ms_capabilities.infrastructure.output.mysql.exceptions.CapabilityNotFoundException;
import com.example.webflux_ms_capabilities.infrastructure.output.mysql.mapper.ICapabilityEntityMapper;
import com.example.webflux_ms_capabilities.infrastructure.output.mysql.repository.CapabilityTechnologyRepository;
import com.example.webflux_ms_capabilities.infrastructure.output.mysql.repository.ICapabilityRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.webflux_ms_capabilities.infrastructure.output.mysql.util.constants.ConstantsErrors.SOME_CAPABILITIES_NOT_FOUND;

@RequiredArgsConstructor
public class CapabilityAdapter implements ICapabilityPersistencePort {

    private final ICapabilityRepository capabilityRepository;
    private final CapabilityTechnologyRepository capabilityTechnologyRepository;
    private final ICapabilityEntityMapper capabilityEntityMapper;

    @Override
    public Mono<Void> saveCapability(CapabilityModel capabilityModel) {
        CapabilityEntity entity = capabilityEntityMapper.toEntity(capabilityModel);

        return capabilityRepository.save(entity)
                .flatMapMany(savedCapability -> {
                    List<CapabilityTechnologyEntity> relations = capabilityModel.getTechnologies().stream()
                            .map(tech -> new CapabilityTechnologyEntity(savedCapability.getId(), tech.getId()))
                            .collect(Collectors.toList());

                    return capabilityTechnologyRepository.saveAll(relations);
                })
                .then();
    }

    @Override
    public Mono<Boolean> existCapabilityByName(String capabilityName) {
        return capabilityRepository.existsCapabilityEntitiesByName(capabilityName);
    }

    @Override
    public Mono<List<CapabilityModel>> getCapabilitiesByIds(List<Long> capabilityIds) {
        return capabilityRepository.findAllById(capabilityIds)
                .collectList()
                .flatMap(capabilities -> {
                    List<Long> foundCapabilityIds = capabilities.stream()
                            .map(CapabilityEntity::getId)
                            .toList();

                    List<Long> missingCapabilityIds = capabilityIds.stream()
                            .filter(id -> !foundCapabilityIds.contains(id))
                            .toList();

                    if (!missingCapabilityIds.isEmpty()) {
                        return Mono.error(new CapabilityNotFoundException(SOME_CAPABILITIES_NOT_FOUND));
                    }
                    return Flux.fromIterable(capabilities)
                            .flatMap(capabilityEntity ->
                                    capabilityTechnologyRepository.findByCapabilityId(capabilityEntity.getId())
                                            .map(relation -> new TechnologyModel(relation.getTechnologyId(), "", ""))
                                            .collectList()
                                            .map(technologies -> {
                                                CapabilityModel capabilityModel = capabilityEntityMapper.toModel(capabilityEntity);
                                                capabilityModel.setTechnologies(technologies);
                                                return capabilityModel;
                                            })
                            )
                            .collectList();
                });
    }
}
