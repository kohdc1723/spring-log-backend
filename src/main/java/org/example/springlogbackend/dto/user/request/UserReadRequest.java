package org.example.springlogbackend.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserReadRequest(@NotBlank String userId) {}
