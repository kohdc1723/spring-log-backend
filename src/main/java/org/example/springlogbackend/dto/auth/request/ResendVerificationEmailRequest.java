package org.example.springlogbackend.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ResendVerificationEmailRequest(
        @NotBlank
        @Email
        String email
) {
}
