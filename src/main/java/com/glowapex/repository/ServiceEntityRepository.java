package com.glowapex.repository;

import com.glowapex.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceEntityRepository extends JpaRepository<ServiceEntity, Long> {
    Optional<ServiceEntity> findByServiceName(String serviceName);
}