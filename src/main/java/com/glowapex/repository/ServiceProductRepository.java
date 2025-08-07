package com.glowapex.repository;

import com.glowapex.entity.ServiceProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceProductRepository extends JpaRepository<ServiceProduct, Long> {
    Optional<ServiceProduct> findByServiceName(String serviceName);
}
