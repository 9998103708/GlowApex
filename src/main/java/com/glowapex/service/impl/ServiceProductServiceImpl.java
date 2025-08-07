package com.glowapex.service.impl;

import com.glowapex.dto.PackageQuantityDTO;
import com.glowapex.dto.ServiceDTO;
import com.glowapex.dto.ServiceProductDTO;
import com.glowapex.entity.PackageQuantity;
import com.glowapex.entity.ServiceEntity;
import com.glowapex.entity.ServicePackage;
import com.glowapex.entity.ServiceProduct;
import com.glowapex.repository.ServiceProductRepository;
import com.glowapex.service.ServiceEntityService;
import com.glowapex.service.ServiceProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceProductServiceImpl implements ServiceProductService {

    private final ServiceProductRepository serviceProductRepository;
    private final ServiceEntityService serviceEntityService;

    @Override
    public ServiceProductDTO createOrUpdateService(ServiceProductDTO dto) {
        ServiceProduct serviceProduct = dto.getId() != null
                ? serviceProductRepository.findById(dto.getId()).orElse(new ServiceProduct())
                : new ServiceProduct();

        serviceProduct.setProviderServiceID(dto.getProviderServiceID());
        serviceProduct.setDescription(dto.getDescription());

        ServiceEntity serviceEntity = serviceEntityService.findByServiceName(dto.getServiceName());
        if (serviceEntity == null) {
            serviceEntity = new ServiceEntity();
            serviceEntity.setServiceName(dto.getServiceName());
            serviceEntity = serviceEntityService.save(serviceEntity);
        }
        serviceProduct.setServiceEntity(serviceEntity);

        // Map service packages
        List<ServicePackage> servicePackages = dto.getPackages().stream().map(packageDTO -> {
            ServicePackage servicePackage = new ServicePackage();
            servicePackage.setName(packageDTO.getName());

            List<PackageQuantity> quantities = packageDTO.getQuantities().stream().map(quantityDTO -> {
                PackageQuantity quantity = new PackageQuantity();
                quantity.setAmount(quantityDTO.getAmount());
                quantity.setPrice(quantityDTO.getPrice());
                quantity.setDiscount(quantityDTO.getDiscount());
                quantity.setServicePackage(servicePackage);
                return quantity;
            }).collect(Collectors.toList());

            servicePackage.setQuantities(quantities);
            servicePackage.setServiceProduct(serviceProduct);
            return servicePackage;
        }).collect(Collectors.toList());

        serviceProduct.setPackages(servicePackages);

        return toDTO(serviceProductRepository.save(serviceProduct));
    }

    @Override
    public List<ServiceProductDTO> getAllServices() {
        return serviceProductRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceProductDTO getServiceById(Long id) {
        return serviceProductRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Service product not found"));
    }

    @Override
    public void deleteService(Long id) {
        serviceProductRepository.deleteById(id);
    }

    @Override
    public ServiceProduct findEntityByServiceName(String serviceName) {
        return serviceProductRepository.findAll().stream()
                .filter(sp -> sp.getServiceEntity() != null && serviceName.equalsIgnoreCase(sp.getServiceEntity().getServiceName()))
                .findFirst()
                .orElse(null);
    }

    private ServiceProductDTO toDTO(ServiceProduct serviceProduct) {
        ServiceProductDTO dto = new ServiceProductDTO();
        dto.setId(serviceProduct.getId());
        dto.setProviderServiceID(serviceProduct.getProviderServiceID());
        dto.setDescription(serviceProduct.getDescription());
        dto.setServiceName(serviceProduct.getServiceEntity().getServiceName());

        List<ServiceDTO> serviceDTOs = new ArrayList<>();
        for (ServicePackage servicePackage : serviceProduct.getPackages()) {
            ServiceDTO serviceDTO = new ServiceDTO();
            serviceDTO.setName(servicePackage.getName());

            List<PackageQuantityDTO> quantityDTOs = new ArrayList<>();
            for (PackageQuantity quantity : servicePackage.getQuantities()) {
                PackageQuantityDTO quantityDTO = new PackageQuantityDTO();
                quantityDTO.setAmount(quantity.getAmount());
                quantityDTO.setPrice(quantity.getPrice());
                quantityDTO.setDiscount(quantity.getDiscount());
                quantityDTOs.add(quantityDTO);
            }

            serviceDTO.setQuantities(quantityDTOs);
            serviceDTOs.add(serviceDTO);
        }

        dto.setPackages(serviceDTOs);
        return dto;
    }
}