package com.divine.autofind.service;

import com.divine.autofind.model.Provider;
import com.divine.autofind.model.ServiceCategory;
import com.divine.autofind.repository.ProviderRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ProviderService {
    private final ProviderRepository providerRepository;

    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    public List<Provider> search(ServiceCategory category, String area) {
        String requestedArea = area == null ? "" : area.trim().toLowerCase(Locale.ROOT);
        return providerRepository.findAll(Sort.by("name")).stream()
                .filter(provider -> category == null || provider.getCategory() == category)
                .filter(provider -> requestedArea.isEmpty()
                        || provider.getTown().toLowerCase(Locale.ROOT).contains(requestedArea)
                        || provider.getCounty().toLowerCase(Locale.ROOT).contains(requestedArea))
                .toList();
    }

    public Provider get(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Service not found"));
    }
}
