package org.example.springlogbackend.dto.auth.response;

import lombok.Builder;
import org.example.springlogbackend.entity.ProviderType;
import org.example.springlogbackend.entity.UserRoleType;

import java.time.Instant;

@Builder
public record SignUpResponse(
        String id,
        String email,
        UserRoleType role,
        ProviderType provider,
        Instant createdAt
) {
}
