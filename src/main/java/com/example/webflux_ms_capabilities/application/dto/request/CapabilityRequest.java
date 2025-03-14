package com.example.webflux_ms_capabilities.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.example.webflux_ms_capabilities.application.utils.constants.ConstantsApplication.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CapabilityRequest {
    @NotBlank(message = IS_REQUIRED)
    private String name;

    @NotBlank(message = IS_REQUIRED)
    private String description;

    @Size(min = MIN_SIZE_TECHNOLOGIES, max = MAX_SIZE_TECHNOLOGIES, message = SIZE_TECHNOLOGIES)
    private List<Long> technologies;
}
