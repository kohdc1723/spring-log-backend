package org.example.springlogbackend.repository;

import org.example.springlogbackend.entity.Account;
import org.example.springlogbackend.entity.ProviderType;
import org.example.springlogbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByUserIdAndProvider(String userId, ProviderType provider);
    Optional<Account> findByProviderAndProviderId(ProviderType provider, String providerId);
    void deleteAllByUserId(String userId);
}
