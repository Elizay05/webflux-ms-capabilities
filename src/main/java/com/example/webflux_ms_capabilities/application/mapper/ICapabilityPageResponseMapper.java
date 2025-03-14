package com.example.webflux_ms_capabilities.application.mapper;

import com.example.webflux_ms_capabilities.application.dto.response.CapabilityPageResponse;
import com.example.webflux_ms_capabilities.domain.model.CapabilityPageModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICapabilityPageResponseMapper {
    CapabilityPageResponse toCapabilityPageResponse(CapabilityPageModel capabilityPageModel);
}
