package com.glowapex.controller;

import com.glowapex.entity.Provider;
import com.glowapex.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/providers")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @PostMapping
    public Provider createProvider(@RequestBody Provider provider) {
        return providerService.createProvider(provider);
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @GetMapping
    public List<Provider> getAllProviders() {
        return providerService.getAllProviders();
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @GetMapping("/{id}")
    public Provider getProviderById(@PathVariable Long id) {
        return providerService.getProviderById(id);
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @PutMapping("/{id}")
    public Provider updateProvider(@PathVariable Long id, @RequestBody Provider updatedProvider) {
        return providerService.updateProvider(id, updatedProvider);
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @DeleteMapping("/{id}")
    public void deleteProvider(@PathVariable Long id) {
        providerService.deleteProvider(id);
    }

    // ✅ Only SuperAdmin can check provider balance
    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @GetMapping("/{id}/balance")
    public Double checkBalance(@PathVariable Long id) {
        return providerService.checkBalance(id);
    }
}