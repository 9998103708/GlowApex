package com.glowapex.service;

import com.glowapex.dto.PackageDTO;
import com.glowapex.dto.PackageQuantityDTO;
import com.glowapex.dto.ServiceDTO;
import com.glowapex.entity.PackageEntity;
import com.glowapex.entity.PackageQuantityEntity;
import com.glowapex.entity.ServiceEntity;
import com.glowapex.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceManagementService {

    @Autowired
    private ServiceRepository serviceRepository;

    public ServiceEntity saveService(ServiceDTO dto) {
        ServiceEntity entity = new ServiceEntity();
        entity.setProviderServiceId(dto.getProviderServiceId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setFeatures(dto.getFeatures());

        List<PackageEntity> packageEntities = new ArrayList<>();
        for (PackageDTO pkg : dto.getPackages()) {
            PackageEntity p = new PackageEntity();
            p.setPackageName(pkg.getPackageName());
            p.setService(entity);

            List<PackageQuantityEntity> quantityEntities = new ArrayList<>();
            for (PackageQuantityDTO q : pkg.getQuantities()) {
                PackageQuantityEntity qe = new PackageQuantityEntity();
                qe.setAmount(q.getAmount());
                qe.setPrice(q.getPrice());
                qe.setDiscount(q.getDiscount());
                qe.setPkg(p);
                quantityEntities.add(qe);
            }

            p.setQuantities(quantityEntities);
            packageEntities.add(p);
        }

        entity.setPackages(packageEntities);
        return serviceRepository.save(entity);
    }

    public List<ServiceEntity> getAllServices() {
        return serviceRepository.findAll();
    }

    public Optional<ServiceEntity> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }

    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }
}
