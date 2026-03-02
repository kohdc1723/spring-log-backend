package org.example.springlogbackend.dto.account;

import lombok.Builder;
import org.example.springlogbackend.entity.ProviderType;

import java.time.Instant;

@Builder
public record AccountResponse(
        String id,
        ProviderType provider,
        String providerId,
        String profileImageUrl,
        Instant createdAt,
        Instant updatedAt
) {
}
