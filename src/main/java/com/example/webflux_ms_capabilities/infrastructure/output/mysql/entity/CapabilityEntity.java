package com.example.webflux_ms_capabilities.infrastructure.output.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("capability")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CapabilityEntity {
    @Id
    private Long id;
    private String name;
    private String description;
}
