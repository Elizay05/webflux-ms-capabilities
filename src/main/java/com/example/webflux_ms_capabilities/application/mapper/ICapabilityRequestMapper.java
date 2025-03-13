package com.example.webflux_ms_capabilities.application.mapper;

import com.example.webflux_ms_capabilities.application.dto.request.CapabilityRequest;
import com.example.webflux_ms_capabilities.domain.model.CapabilityModel;
import com.example.webflux_ms_capabilities.domain.model.TechnologyModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICapabilityRequestMapper {

    @Mapping(target = "technologies", source = "technologies", qualifiedByName = "mapTechnologyIds")
    CapabilityModel toCapabilityModel(CapabilityRequest request);

    @Named("mapTechnologyIds")
    default List<TechnologyModel> mapTechnologyIds(List<Long> technologyIds) {
        return technologyIds.stream()
                .map(id -> new TechnologyModel(id, "", ""))
                .collect(Collectors.toList());
    }
}
