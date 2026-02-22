package org.example.springlogbackend.dto.auth.oauth2;

import lombok.Builder;

@Builder
public record TokenResponse(String accessToken) {}
