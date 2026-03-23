package org.example.springlogbackend.dto.user.response;

import lombok.Builder;
import org.example.springlogbackend.dto.auth.response.AccountResponse;
import org.example.springlogbackend.dto.auth.response.RefreshTokenResponse;
import org.example.springlogbackend.entity.UserRoleType;

import java.time.Instant;
import java.util.List;

@Builder
public record UserResponse(
        String id,
        String email,
        UserRoleType role,
        Instant createdAt,
        Instant updatedAt,
        List<AccountResponse> accounts,
        List<RefreshTokenResponse> refreshTokens
) {}
