package org.example.springlogbackend.dto.user.request;

import lombok.Builder;

@Builder
public record UserDeleteRequest(String userId) {}
