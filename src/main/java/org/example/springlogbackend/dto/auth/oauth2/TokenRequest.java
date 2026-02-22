package org.example.springlogbackend.dto.auth.oauth2;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
        @NotBlank String code
) {}
