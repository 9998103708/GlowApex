package com.glowapex.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "service_entities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider_service_id", nullable = false)
    private String providerServiceID;

    @Column(name = "service_name", nullable = false, unique = true)
    private String serviceName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "service_features", joinColumns = @JoinColumn(name = "service_entity_id"))
    @Column(name = "feature")
    private List<String> features;

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

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }
}
