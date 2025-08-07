package com.glowapex.service;

import com.glowapex.entity.ServiceEntity;

import java.util.List;

public interface ServiceEntityService {
    ServiceEntity save(ServiceEntity serviceEntity);

    List<ServiceEntity> findAll();

    ServiceEntity findById(Long id);

    ServiceEntity update(Long id, ServiceEntity updatedService);

    void delete(Long id);

    ServiceEntity findByServiceName(String serviceName);
}
