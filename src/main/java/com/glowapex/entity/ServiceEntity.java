package com.glowapex.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "services")
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String providerServiceId;

    private String name;

    private String description;

    @ElementCollection
    @CollectionTable(name = "service_features", joinColumns = @JoinColumn(name = "service_id"))
    @Column(name = "feature")
    private List<String> features;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackageEntity> packages;

    // Getters and Setters

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

    public List<PackageEntity> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageEntity> packages) {
        this.packages = packages;

        // Set this service reference in each package (IMPORTANT!)
        if (packages != null) {
            for (PackageEntity p : packages) {
                p.setService(this);
            }
        }
    }
}