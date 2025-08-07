package com.glowapex.dto;

import lombok.*;

@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class PackageQuantityDTO {
    private Long id;
    private int amount;
    private double price;
    private double discount;

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
}
