package com.glowapex.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProductDTO {
    private Long id;
    private String providerServiceID;
    private String serviceName;
    private String description;
    private String features;
    private List<ServiceDTO> packages; // List of service packages (with name and quantities)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProviderServiceID() {
        return providerServiceID;
    }

    public void setProviderServiceID(String providerServiceID) {
        this.providerServiceID = providerServiceID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public List<ServiceDTO> getPackages() {
        return packages;
    }

    public void setPackages(List<ServiceDTO> packages) {
        this.packages = packages;
    }
}
