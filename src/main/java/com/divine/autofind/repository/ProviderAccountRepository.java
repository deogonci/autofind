package com.divine.autofind.repository;

import com.divine.autofind.model.ProviderAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderAccountRepository extends JpaRepository<ProviderAccount, Long> {
    @EntityGraph(attributePaths = "provider")
    Optional<ProviderAccount> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}
