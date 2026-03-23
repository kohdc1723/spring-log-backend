package org.example.springlogbackend.dto.auth;

import lombok.Builder;

@Builder
public record JwtTokenPair(
        String accessToken,
        String refreshToken
) {}
