package com.glowapex.dto;

import lombok.Data;

import java.util.List;

@Data
public class ServiceDTO {
    private Long id;
    private String providerServiceId;
    private String name;
    private String description;
    private String features;
    private List<PackageDTO> packages;
}
