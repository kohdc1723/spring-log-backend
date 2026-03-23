package org.example.springlogbackend.repository;

import org.example.springlogbackend.entity.EmailVerificationToken;
import org.example.springlogbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, String> {
    Optional<EmailVerificationToken> findByToken(String token);
    Optional<EmailVerificationToken> findFirstByUserOrderByCreatedAtDesc(User user);
    void deleteByExpiresAtBefore(Instant now);
    void deleteByUserId(String userId);
}
