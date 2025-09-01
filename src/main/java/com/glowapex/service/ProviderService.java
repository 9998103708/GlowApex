package com.glowapex.service;

import com.glowapex.entity.Provider;
import com.glowapex.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    public Provider createProvider(Provider provider) {
        return providerRepository.save(provider);
    }

    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }

    public Provider getProviderById(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Provider not found"));
    }

    public Provider updateProvider(Long id, Provider updatedProvider) {
        Provider provider = getProviderById(id);
        provider.setProviderName(updatedProvider.getProviderName());
        provider.setAlias(updatedProvider.getAlias());
        provider.setBalance(updatedProvider.getBalance());
        provider.setStatus(updatedProvider.getStatus());
        provider.setApiKey(updatedProvider.getApiKey());
        return providerRepository.save(provider);
    }

    public void deleteProvider(Long id) {
        providerRepository.deleteById(id);
    }

    public Double checkBalance(Long id) {
        Provider provider = getProviderById(id);
        return provider.getBalance();
    }
}