package com.example.webflux_ms_capabilities.infrastructure.output.mysql.mapper;

import com.example.webflux_ms_capabilities.domain.model.CapabilityModel;
import com.example.webflux_ms_capabilities.infrastructure.output.mysql.entity.CapabilityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICapabilityEntityMapper {
    CapabilityEntity toEntity(CapabilityModel model);
    CapabilityModel toModel(CapabilityEntity entity);
}
