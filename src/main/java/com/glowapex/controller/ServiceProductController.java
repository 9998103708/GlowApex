package com.glowapex.controller;

import com.glowapex.dto.ServiceProductDTO;
import com.glowapex.service.ServiceProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/services")
@RequiredArgsConstructor
public class ServiceProductController {

    private final ServiceProductService serviceProductService;

    /**
     * Create or update a service product with nested packages and quantities.
     * Secured for ADMIN only.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ServiceProductDTO> saveOrUpdate(@RequestBody ServiceProductDTO dto) {
        ServiceProductDTO savedDto = serviceProductService.createOrUpdateService(dto);
        return ResponseEntity.ok(savedDto);
    }

    /**
     * Get all service products with packages and quantities.
     */
    @GetMapping
    public ResponseEntity<List<ServiceProductDTO>> getAll() {
        List<ServiceProductDTO> allServices = serviceProductService.getAllServices();
        return ResponseEntity.ok(allServices);
    }

    /**
     * Get a specific service product by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceProductDTO> getById(@PathVariable Long id) {
        ServiceProductDTO service = serviceProductService.getServiceById(id);
        return ResponseEntity.ok(service);
    }

    /**
     * Delete a service product by ID.
     * Secured for ADMIN only.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        serviceProductService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}