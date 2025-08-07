package com.glowapex.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDTO {
    private Long id;
    private String name; // e.g., "Basic", "Premium"
    private List<PackageQuantityDTO> quantities;

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

    public List<PackageQuantityDTO> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<PackageQuantityDTO> quantities) {
        this.quantities = quantities;
    }
}
