package org.example.springlogbackend.dto.auth.request;

public record LoginRequest(
        String email,
        String password
) {
}
