package com.glowapex.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_packages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicePackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., "Basic", "Premium"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_product_id")
    private ServiceProduct serviceProduct;

    @OneToMany(mappedBy = "servicePackage", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PackageQuantity> quantities = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServiceProduct getServiceProduct() {
        return serviceProduct;
    }

    public void setServiceProduct(ServiceProduct serviceProduct) {
        this.serviceProduct = serviceProduct;
    }

    public List<PackageQuantity> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<PackageQuantity> quantities) {
        this.quantities = quantities;
    }
}
