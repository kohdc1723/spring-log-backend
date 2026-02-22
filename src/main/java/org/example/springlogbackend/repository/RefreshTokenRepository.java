package org.example.springlogbackend.repository;

import org.example.springlogbackend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    void deleteAllByUserId(String userId);
    void deleteByToken(String token);
    void deleteByCreatedAtBefore(LocalDateTime createdAt);
    Optional<RefreshToken> findByToken(String token);
}
