package com.glowapex.controller;

import com.glowapex.entity.ProviderService;
import com.glowapex.service.ProviderServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/provider-services")
public class ProviderServiceController {

    @Autowired
    private ProviderServiceService providerServiceService;

    @PostMapping
    public ProviderService createProviderService(@RequestBody ProviderService providerService) {
        return providerServiceService.createProviderService(providerService);
    }

    @GetMapping
    public List<ProviderService> getAllProviderServices() {
        return providerServiceService.getAllProviderServices();
    }

    @GetMapping("/{id}")
    public ProviderService getProviderServiceById(@PathVariable Long id) {
        return providerServiceService.getProviderServiceById(id);
    }

    @PutMapping("/{id}")
    public ProviderService updateProviderService(@PathVariable Long id, @RequestBody ProviderService updatedService) {
        return providerServiceService.updateProviderService(id, updatedService);
    }

    @DeleteMapping("/{id}")
    public void deleteProviderService(@PathVariable Long id) {
        providerServiceService.deleteProviderService(id);
    }
}
