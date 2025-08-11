package com.glowapex.entity;


import javax.persistence.*;
import java.util.List;

@Entity
public class ServicePackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String packageName;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "service_package_id")
    private List<PackageQuantity> packageQuantities;

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<PackageQuantity> getPackageQuantities() {
        return packageQuantities;
    }

    public void setPackageQuantities(List<PackageQuantity> packageQuantities) {
        this.packageQuantities = packageQuantities;
    }
}
