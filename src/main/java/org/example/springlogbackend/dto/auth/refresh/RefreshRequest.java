package org.example.springlogbackend.dto.auth.refresh;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RefreshRequest(@NotBlank String refreshToken) {}
