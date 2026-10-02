package com.divine.autofind.repository;

import com.divine.autofind.model.Booking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @EntityGraph(attributePaths = "provider")
    Optional<Booking> findByReference(String reference);
    List<Booking> findByProvider_IdOrderByCreatedAtDesc(Long providerId);
    Optional<Booking> findByIdAndProvider_Id(Long id, Long providerId);
}
