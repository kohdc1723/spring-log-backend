package org.example.springlogbackend.repository;

import org.example.springlogbackend.entity.PasswordResetToken;
import org.example.springlogbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findFirstByUserOrderByCreatedAtDesc(User user);
    void deleteByUserId(String userId);
    void deleteByExpiresAtBefore(Instant now);
}
