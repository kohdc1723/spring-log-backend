package org.example.springlogbackend.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ResetPasswordRequest(
        @NotBlank
        String token,
        @NotBlank @Size(min = 6)
        String newPassword
) {
}
