package com.example.webflux_ms_capabilities.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CapabilityIdsRequest {
    private List<Long> capabilityIds;
}
