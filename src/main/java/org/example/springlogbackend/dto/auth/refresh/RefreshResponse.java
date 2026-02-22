package org.example.springlogbackend.dto.auth.refresh;

import lombok.Builder;

@Builder
public record RefreshResponse(String refreshToken) {}
