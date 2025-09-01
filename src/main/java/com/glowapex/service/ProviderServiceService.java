package com.glowapex.service;

import com.glowapex.entity.ProviderService;
import com.glowapex.repository.ProviderServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderServiceService {

    @Autowired
    private ProviderServiceRepository providerServiceRepository;

    public ProviderService createProviderService(ProviderService providerService) {
        return providerServiceRepository.save(providerService);
    }

    public List<ProviderService> getAllProviderServices() {
        return providerServiceRepository.findAll();
    }

    public ProviderService getProviderServiceById(Long id) {
        return providerServiceRepository.findById(id).orElse(null);
    }

    public ProviderService updateProviderService(Long id, ProviderService updatedService) {
        return providerServiceRepository.findById(id).map(service -> {
            service.setProviderServiceId(updatedService.getProviderServiceId());
            service.setServiceName(updatedService.getServiceName());
            service.setDescription(updatedService.getDescription());
            service.setFeatures(updatedService.getFeatures());

            // fix for packages
            service.getPackages().clear();
            if (updatedService.getPackages() != null) {
                service.getPackages().addAll(updatedService.getPackages());
            }

            return providerServiceRepository.save(service);
        }).orElse(null);
    }

    public void deleteProviderService(Long id) {
        providerServiceRepository.deleteById(id);
    }
}
