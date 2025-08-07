package com.glowapex.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "package_quantities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageQuantity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amount;   // e.g., 500, 1000 views

    private double price; // Price for this quantity

    private double discount; // Optional discount (e.g., 10.0 = 10% off)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_package_id")
    private ServicePackage servicePackage;

    public void setId(Long id) {
        this.id = id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setServicePackage(ServicePackage servicePackage) {
        this.servicePackage = servicePackage;
    }
}
