package org.example.springlogbackend.dto.auth.response;

import lombok.Builder;

@Builder
public record RefreshResponse(String refreshToken) {}
