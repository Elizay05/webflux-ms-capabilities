package com.example.webflux_ms_capabilities.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CapabilityRequest {
    @NotBlank(message = "es requerido")
    private String name;

    @NotBlank(message = "es requerido")
    private String description;

    @Size(min = 3, max = 20, message = "Las tecnologías deben estar entre 3 y 20")
    private List<Long> technologies;
}
