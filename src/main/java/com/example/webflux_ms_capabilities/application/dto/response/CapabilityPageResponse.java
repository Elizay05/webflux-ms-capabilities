package com.example.webflux_ms_capabilities.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CapabilityPageResponse {
    private List<CapabilityResponse> capabilities;
    private int totalPages;
    private long totalElements;
}
