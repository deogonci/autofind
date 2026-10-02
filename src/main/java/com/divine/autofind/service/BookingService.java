package com.divine.autofind.service;

import com.divine.autofind.model.Booking;
import com.divine.autofind.model.BookingStatus;
import com.divine.autofind.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class BookingService {
    private final BookingRepository bookings;
    private final ProviderService providers;

    public BookingService(BookingRepository bookings, ProviderService providers) {
        this.bookings = bookings;
        this.providers = providers;
    }

    public Booking request(Long providerId, String name, String email,
                           LocalDate preferredDate, String notes) {
        Booking booking = new Booking(providers.get(providerId), name.trim(), email.trim(),
                preferredDate, notes == null ? "" : notes.trim());
        return bookings.save(booking);
    }

    public Booking getByReference(String reference) {
        return bookings.findByReference(reference)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Request not found"));
    }

    public List<Booking> forProvider(Long providerId) {
        return bookings.findByProvider_IdOrderByCreatedAtDesc(providerId);
    }

    @Transactional
    public void updateStatus(Long bookingId, Long providerId, BookingStatus status) {
        Booking booking = bookings.findByIdAndProvider_Id(bookingId, providerId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Request not found"));
        booking.setStatus(status);
    }
}
