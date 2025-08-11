package com.glowapex.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class ProviderService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String providerServiceId;
    private String serviceName;

    @Column(length = 2000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "provider_service_features", joinColumns = @JoinColumn(name = "provider_service_id"))
    @Column(name = "feature")
    private List<String> features;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "provider_service_id")
    private List<ServicePackage> packages;

    // Getters & Setters
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

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public List<ServicePackage> getPackages() {
        return packages;
    }

    public void setPackages(List<ServicePackage> packages) {
        this.packages = packages;
    }
}
