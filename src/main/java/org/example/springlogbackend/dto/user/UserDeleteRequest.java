package org.example.springlogbackend.dto.user;

import lombok.Builder;

@Builder
public record UserDeleteRequest(String userId) {}
