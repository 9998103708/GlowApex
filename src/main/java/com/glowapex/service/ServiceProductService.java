package com.glowapex.service;

import com.glowapex.dto.ServiceProductDTO;
import com.glowapex.entity.ServiceProduct;

import java.util.List;

public interface ServiceProductService {
    ServiceProductDTO createOrUpdateService(ServiceProductDTO dto);

    List<ServiceProductDTO> getAllServices();

    ServiceProductDTO getServiceById(Long id);

    void deleteService(Long id);

    // ✅ New method for order validation
    ServiceProduct findEntityByServiceName(String serviceName);
}
