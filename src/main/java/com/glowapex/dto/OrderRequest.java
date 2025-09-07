package com.glowapex.dto;

import java.math.BigDecimal;

public class OrderRequest {
    private Long userId;
    private String serviceName;
    private int quantity;
    private BigDecimal price;
    private String link;

    // Getters & Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
}