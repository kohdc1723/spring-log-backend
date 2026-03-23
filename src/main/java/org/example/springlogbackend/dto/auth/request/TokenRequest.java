package org.example.springlogbackend.dto.auth.request;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(@NotBlank String code) {}
