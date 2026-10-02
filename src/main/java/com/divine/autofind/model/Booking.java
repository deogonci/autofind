package com.divine.autofind.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reference = UUID.randomUUID().toString();
    private String customerName;
    private String email;
    private LocalDate preferredDate;
    private String notes;
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.REQUESTED;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "provider_id")
    private Provider provider;

    protected Booking() {
    }

    public Booking(Provider provider, String customerName, String email,
                   LocalDate preferredDate, String notes) {
        this.provider = provider;
        this.customerName = customerName;
        this.email = email;
        this.preferredDate = preferredDate;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getPreferredDate() {
        return preferredDate;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BookingStatus getStatus() {
        return status == null ? BookingStatus.REQUESTED : status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Provider getProvider() {
        return provider;
    }
}
