package com.glowapex.dto;

import lombok.Data;

import java.util.List;


public class ServiceDTO {
    private Long id;
    private String providerServiceId;
    private String name;
    private String description;
    private List<String> features;
    private List<PackageDTO> packages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProviderServiceId() {
        return providerServiceId;
    }

    public void setProviderServiceId(String providerServiceId) {
        this.providerServiceId = providerServiceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public List<PackageDTO> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageDTO> packages) {
        this.packages = packages;
    }
}
