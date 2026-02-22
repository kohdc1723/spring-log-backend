package org.example.springlogbackend.util;

import lombok.Getter;

@Getter
public enum JwtTokenType {
    ACCESS("access"),
    REFRESH("refresh");

    private final String value;

    JwtTokenType(final String value) {
        this.value = value;
    }
}
