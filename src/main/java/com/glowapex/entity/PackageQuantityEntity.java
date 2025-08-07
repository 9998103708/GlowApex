package com.glowapex.entity;

import javax.persistence.*;

@Entity
@Table(name = "package_quantities")
public class PackageQuantityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amount;
    private double price;
    private double discount;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private PackageEntity pkg;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public PackageEntity getPkg() {
        return pkg;
    }

    public void setPkg(PackageEntity pkg) {
        this.pkg = pkg;
    }
}
