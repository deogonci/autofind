package com.divine.autofind.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class BookingRequestForm {
    @NotBlank(message = "Enter your name")
    @Size(max = 80)
    private String name;

    @NotBlank(message = "Enter your email")
    @Email(message = "Enter a valid email")
    @Size(max = 120)
    private String email;

    @NotNull(message = "Choose a date")
    @FutureOrPresent(message = "Choose today or a future date")
    private LocalDate preferredDate;

    @Size(max = 240, message = "Keep the message under 240 characters")
    private String notes;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getPreferredDate() { return preferredDate; }
    public void setPreferredDate(LocalDate preferredDate) { this.preferredDate = preferredDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
