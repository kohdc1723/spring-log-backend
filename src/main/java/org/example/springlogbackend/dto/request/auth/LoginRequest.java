package org.example.springlogbackend.dto.request.auth;

public record LoginRequest(
        String email,
        String password
) {
}
