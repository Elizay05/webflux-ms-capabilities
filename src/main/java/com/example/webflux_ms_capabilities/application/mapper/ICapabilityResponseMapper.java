package com.example.webflux_ms_capabilities.application.mapper;

import com.example.webflux_ms_capabilities.application.dto.response.CapabilityResponse;
import com.example.webflux_ms_capabilities.domain.model.CapabilityModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICapabilityResponseMapper {
    List<CapabilityResponse> toListCapabilityResponse(List<CapabilityModel> capabilityModel);
}
