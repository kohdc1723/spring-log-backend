package org.example.springlogbackend.dto.user;

import lombok.Builder;
import org.example.springlogbackend.dto.account.AccountResponse;
import org.example.springlogbackend.dto.refreshtoken.RefreshTokenResponse;
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
