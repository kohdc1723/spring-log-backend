package org.example.springlogbackend.dto.user;

import lombok.Builder;
import org.example.springlogbackend.entity.UserRoleType;

import java.time.LocalDateTime;

@Builder
public record UserResponse(
        String userId,
        String email,
        UserRoleType role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
