package com.glowapex.dto;

import lombok.Data;

import java.util.List;

@Data
public class PackageDTO {
    private String packageName;
    private List<PackageQuantityDTO> quantities;
}
