package org.example.springlogbackend.dto.auth;

import lombok.Builder;

@Builder
public record AccessTokenResponse(String accessToken) {}
