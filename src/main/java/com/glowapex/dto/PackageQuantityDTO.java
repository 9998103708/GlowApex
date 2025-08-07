package com.glowapex.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PackageQuantityDTO {
    private int amount;
    private double price;
    private double discount;
}
