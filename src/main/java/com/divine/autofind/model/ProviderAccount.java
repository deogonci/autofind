package com.divine.autofind.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class ProviderAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "provider_id", unique = true)
    private Provider provider;

    protected ProviderAccount() { }

    public ProviderAccount(String email, String passwordHash, Provider provider) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.provider = provider;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Provider getProvider() { return provider; }
}
