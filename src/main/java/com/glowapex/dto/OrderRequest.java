package com.glowapex.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String email;
    private String serviceName;
    private int quantity;
    private double price;
}