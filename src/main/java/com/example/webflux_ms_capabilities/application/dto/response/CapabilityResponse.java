package com.example.webflux_ms_capabilities.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CapabilityResponse {
    private Long id;
    private String name;
    private String description;
    private List<TechnologyResponse> technologies;
}
