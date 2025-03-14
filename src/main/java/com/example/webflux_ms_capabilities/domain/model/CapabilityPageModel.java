package com.example.webflux_ms_capabilities.domain.model;

import java.util.List;

public class CapabilityPageModel {
    private List<CapabilityModel> capabilities;
    private int totalPages;
    private long totalElements;

    public CapabilityPageModel(List<CapabilityModel> capabilities, int totalPages, long totalElements) {
        this.capabilities = capabilities;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public List<CapabilityModel> getCapabilities() {
        return capabilities;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setCapabilities(List<CapabilityModel> capabilities) {
        this.capabilities = capabilities;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
