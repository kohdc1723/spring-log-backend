package org.example.springlogbackend.dto.auth.response;

import lombok.Builder;
import org.example.springlogbackend.entity.ProviderType;
import org.example.springlogbackend.entity.UserRoleType;

@Builder
public record AuthUserResponse(
        String email,
        UserRoleType role,
        ProviderType provider,
        String profileImageUrl
) {}
