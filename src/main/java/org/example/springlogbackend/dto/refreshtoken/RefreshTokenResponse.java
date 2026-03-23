package org.example.springlogbackend.dto.refreshtoken;

import lombok.Builder;

import java.time.Instant;

@Builder
public record RefreshTokenResponse(
        String id,
        String token,
        String prevToken,
        Instant rotatedAt,
        Instant createdAt
) {
}
