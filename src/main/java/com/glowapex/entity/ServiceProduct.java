package com.glowapex.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String providerServiceID;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_entity_id")
    private ServiceEntity serviceEntity;
    private String serviceName;

    @OneToMany(mappedBy = "serviceProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServicePackage> packages;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ServiceEntity getServiceEntity() {
        return serviceEntity;
    }

    public void setServiceEntity(ServiceEntity serviceEntity) {
        this.serviceEntity = serviceEntity;
    }

    public List<ServicePackage> getPackages() {
        return packages;
    }

    public void setPackages(List<ServicePackage> packages) {
        this.packages = packages;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
