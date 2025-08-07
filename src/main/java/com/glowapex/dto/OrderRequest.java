package com.glowapex.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String email;
    private String serviceName;   // e.g. "YouTube Views"
    private String packageName;   // e.g. "Basic Package"
    private int quantity;         // e.g. 1000
    private String link;          // URL or channel link
    private double price;     // 💰 Price expected by user, used for validation

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
