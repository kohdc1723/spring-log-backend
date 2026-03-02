package org.example.springlogbackend.dto.refreshtoken;

import lombok.Builder;

@Builder
public record RefreshTokenResponse(
        String token
) {
}
