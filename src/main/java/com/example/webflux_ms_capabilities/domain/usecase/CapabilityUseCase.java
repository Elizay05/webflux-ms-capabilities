package com.example.webflux_ms_capabilities.domain.usecase;

import com.example.webflux_ms_capabilities.domain.api.ICapabilityServicePort;
import com.example.webflux_ms_capabilities.domain.exceptions.CapabilityAlreadyExistsException;
import com.example.webflux_ms_capabilities.domain.exceptions.DuplicateTechnologiesException;
import com.example.webflux_ms_capabilities.domain.exceptions.MaxTechnologiesCapabilityException;
import com.example.webflux_ms_capabilities.domain.exceptions.MinTechnologiesCapabilityException;
import com.example.webflux_ms_capabilities.domain.model.CapabilityModel;
import com.example.webflux_ms_capabilities.domain.model.TechnologyModel;
import com.example.webflux_ms_capabilities.domain.spi.ICapabilityPersistencePort;
import com.example.webflux_ms_capabilities.domain.spi.ITechnologyPersistencePort;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.webflux_ms_capabilities.domain.utils.constants.ConstantsDomain.*;

public class CapabilityUseCase implements ICapabilityServicePort {

    private final ICapabilityPersistencePort capabilityPersistencePort;
    private final ITechnologyPersistencePort technologyPersistencePort;

    public CapabilityUseCase(ICapabilityPersistencePort capabilityPersistencePort, ITechnologyPersistencePort technologyPersistencePort) {
        this.capabilityPersistencePort = capabilityPersistencePort;
        this.technologyPersistencePort = technologyPersistencePort;
    }

    @Override
    public Mono<Void> createCapability(CapabilityModel capabilityModel) {
        return validateCapability(capabilityModel)
                .then(verifyTechnologiesExist(capabilityModel.getTechnologies()))
                .flatMap(technologies -> {
                    capabilityModel.setTechnologies(technologies);
                    return existCapability(capabilityModel.getName());
                })
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new CapabilityAlreadyExistsException(CAPABILITY_ALREADY_EXISTS));
                    }
                    return capabilityPersistencePort.saveCapability(capabilityModel);
                });
    }

    private Mono<List<TechnologyModel>> verifyTechnologiesExist(List<TechnologyModel> technologies) {
        List<Long> technologyIds = technologies.stream()
                .map(TechnologyModel::getId)
                .collect(Collectors.toList());

        return technologyPersistencePort.getTechnologiesByIds(technologyIds);
    }

    private Mono<Void> validateCapability(CapabilityModel capabilityModel) {
        List<TechnologyModel> technologies = capabilityModel.getTechnologies();
        if (technologies.size() < MIN_TECHNOLOGIES_CAPABILITY) {
            return Mono.error(new MinTechnologiesCapabilityException(MIN_TECHNOLOGIES_CAPABILITY_ERROR));
        }
        if (technologies.size() > MAX_TECHNOLOGIES_CAPABILITY) {
            return Mono.error(new MaxTechnologiesCapabilityException(MAX_TECHNOLOGIES_CAPABILITY_ERROR));
        }

        long uniqueTechCount = technologies.stream()
                .map(TechnologyModel::getId)
                .distinct()
                .count();
        if (uniqueTechCount != technologies.size()) {
            return Mono.error(new DuplicateTechnologiesException(DUPLICATE_TECHNOLOGIES_CAPABILITY_ERROR));
        }
        return Mono.just(capabilityModel).then();
    }

    private Mono<Boolean> existCapability(String capabilityName) {
        return capabilityPersistencePort.existCapabilityByName(capabilityName);
    }

    @Override
    public Mono<List<CapabilityModel>> getCapabilitiesByIds(List<Long> capabilityIds) {
        return capabilityPersistencePort.getCapabilitiesByIds(capabilityIds)
                .flatMap(capabilities -> {
                    List<Long> technologyIds = capabilities.stream()
                            .flatMap(cap -> cap.getTechnologies().stream().map(TechnologyModel::getId))
                            .distinct()
                            .collect(Collectors.toList());

                    return technologyPersistencePort.getTechnologiesByIds(technologyIds)
                            .map(technologyList -> {
                                Map<Long, TechnologyModel> technologyMap = technologyList.stream()
                                        .collect(Collectors.toMap(TechnologyModel::getId, tech -> tech));

                                capabilities.forEach(capability ->
                                        capability.setTechnologies(capability.getTechnologies().stream()
                                                .map(tech -> technologyMap.getOrDefault(tech.getId(), tech))
                                                .collect(Collectors.toList()))
                                );

                                return capabilities;
                            });
                });
    }
}
