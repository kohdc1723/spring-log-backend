package org.example.springlogbackend.repository;

import org.example.springlogbackend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    void deleteByCreatedAtBefore(Instant createdAt);
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByTokenOrPrevToken(String token, String prevToken);
}
