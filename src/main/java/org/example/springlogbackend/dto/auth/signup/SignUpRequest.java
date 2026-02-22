package org.example.springlogbackend.dto.auth.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @Email
        String email,
        @NotBlank
        @Size(min = 6)
        String password
) {
}
