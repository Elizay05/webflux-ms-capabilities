package com.example.webflux_ms_capabilities.domain.model;

import java.util.List;

public class CapabilityModel {
    private Long id;
    private String name;
    private String description;
    private List<TechnologyModel> technologies;

    public CapabilityModel(Long id, String name, String description, List<TechnologyModel> technologies) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.technologies = technologies;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<TechnologyModel> getTechnologies() {
        return technologies;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTechnologies(List<TechnologyModel> technologies) {
        this.technologies = technologies;
    }
}
