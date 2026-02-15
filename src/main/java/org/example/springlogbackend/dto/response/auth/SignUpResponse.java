package org.example.springlogbackend.dto.response.auth;

import lombok.Builder;
import org.example.springlogbackend.entity.ProviderType;
import org.example.springlogbackend.entity.UserRoleType;

import java.time.LocalDateTime;

@Builder
public record SignUpResponse(
        String id,
        String email,
        UserRoleType role,
        ProviderType provider,
        LocalDateTime createdAt
) {
}
