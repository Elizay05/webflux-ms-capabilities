package com.example.webflux_ms_capabilities.infrastructure.output.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table("capability_technology")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapabilityTechnologyEntity {
    private Long capabilityId;
    private Long technologyId;
}
