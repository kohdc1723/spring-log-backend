package org.example.springlogbackend.dto.auth;

import lombok.Builder;
import org.example.springlogbackend.entity.ProviderType;

@Builder
public record UserPrincipal(String userId, ProviderType provider) {
}
