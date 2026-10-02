package com.divine.autofind.service;

import com.divine.autofind.controller.ProviderRegistrationForm;
import com.divine.autofind.model.Provider;
import com.divine.autofind.model.ProviderAccount;
import com.divine.autofind.repository.ProviderAccountRepository;
import com.divine.autofind.repository.ProviderRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ProviderAccountService {
    private final ProviderAccountRepository accounts;
    private final ProviderRepository providers;
    private final PasswordEncoder passwords;

    public ProviderAccountService(ProviderAccountRepository accounts,
                                  ProviderRepository providers, PasswordEncoder passwords) {
        this.accounts = accounts;
        this.providers = providers;
        this.passwords = passwords;
    }

    public boolean emailExists(String email) {
        return email != null && accounts.existsByEmailIgnoreCase(email.trim());
    }

    @Transactional
    public void register(ProviderRegistrationForm form) {
        String email = form.getEmail().trim().toLowerCase(Locale.ROOT);
        Provider provider = providers.save(new Provider(form.getName().trim(),
                form.getCategory(), form.getTown().trim(), form.getCounty().trim(),
                form.getTagline().trim(), form.getDescription().trim(), form.getPriceFrom()));
        accounts.save(new ProviderAccount(email, passwords.encode(form.getPassword()), provider));
    }

    public ProviderAccount getByEmail(String email) {
        return accounts.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Provider not found"));
    }
}
