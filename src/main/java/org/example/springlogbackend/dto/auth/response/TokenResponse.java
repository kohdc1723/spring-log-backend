package org.example.springlogbackend.dto.auth.response;

import lombok.Builder;

@Builder
public record TokenResponse(String accessToken) {}
