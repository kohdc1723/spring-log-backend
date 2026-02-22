package org.example.springlogbackend.dto.auth.login;

public record LoginRequest(
        String email,
        String password
) {
}
