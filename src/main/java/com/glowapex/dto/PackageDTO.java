package com.glowapex.dto;

import lombok.Data;

import java.util.List;

public class PackageDTO {
    private String packageName;
    private List<PackageQuantityDTO> quantities;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<PackageQuantityDTO> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<PackageQuantityDTO> quantities) {
        this.quantities = quantities;
    }
}
