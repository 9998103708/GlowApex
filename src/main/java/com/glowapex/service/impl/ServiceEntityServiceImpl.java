package com.glowapex.service.impl;

import com.glowapex.entity.ServiceEntity;
import com.glowapex.repository.ServiceEntityRepository;
import com.glowapex.service.ServiceEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceEntityServiceImpl implements ServiceEntityService {

    private final ServiceEntityRepository serviceEntityRepository;

    @Override
    public ServiceEntity save(ServiceEntity serviceEntity) {
        return serviceEntityRepository.save(serviceEntity);
    }

    @Override
    public List<ServiceEntity> findAll() {
        return serviceEntityRepository.findAll();
    }

    @Override
    public ServiceEntity findById(Long id) {
        return serviceEntityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ServiceEntity not found with id: " + id));
    }

    @Override
    public ServiceEntity update(Long id, ServiceEntity updatedService) {
        ServiceEntity existing = findById(id);
        existing.setServiceName(updatedService.getServiceName());
        existing.setProviderServiceID(updatedService.getProviderServiceID());
        existing.setDescription(updatedService.getDescription());
        existing.setFeatures(updatedService.getFeatures());
        return serviceEntityRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        serviceEntityRepository.deleteById(id);
    }

    @Override
    public ServiceEntity findByServiceName(String serviceName) {
        return serviceEntityRepository.findByServiceName(serviceName)
                .orElseThrow(() -> new RuntimeException("ServiceEntity not found with name: " + serviceName));
    }
}