package com.divine.autofind.service;

import com.divine.autofind.model.Provider;
import com.divine.autofind.model.ServiceCategory;
import com.divine.autofind.repository.ProviderRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ProviderService {
    private final ProviderRepository providers;

    public ProviderService(ProviderRepository providers) {
        this.providers = providers;
    }

    public List<Provider> search(ServiceCategory category, String area) {
        String requestedArea = area == null ? "" : area.trim().toLowerCase();
        return providers.findAll(Sort.by("name")).stream()
                .filter(provider -> category == null || provider.getCategory() == category)
                .filter(provider -> requestedArea.isEmpty()
                        || provider.getTown().toLowerCase().contains(requestedArea)
                        || provider.getCounty().toLowerCase().contains(requestedArea))
                .toList();
    }

    public Provider get(Long id) {
        return providers.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Service not found"));
    }
}
