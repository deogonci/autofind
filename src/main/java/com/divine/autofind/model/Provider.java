package com.divine.autofind.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ServiceCategory category;

    @NotBlank
    private String town;

    @NotBlank
    private String county;

    @NotBlank
    private String tagline;

    @NotBlank
    private String description;

    @NotNull
    private BigDecimal priceFrom;

    protected Provider() {
    }

    public Provider(String name, ServiceCategory category, String town,
                    String county, String tagline, String description, BigDecimal priceFrom) {
        this.name = name;
        this.category = category;
        this.town = town;
        this.county = county;
        this.tagline = tagline;
        this.description = description;
        this.priceFrom = priceFrom;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ServiceCategory getCategory() {
        return category;
    }

    public String getTown() {
        return town;
    }

    public String getCounty() {
        return county;
    }

    public String getTagline() {
        return tagline;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPriceFrom() {
        return priceFrom;
    }
}
