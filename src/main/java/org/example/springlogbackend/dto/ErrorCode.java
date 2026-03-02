package org.example.springlogbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    INVALID_AUTH_CODE(HttpStatus.UNAUTHORIZED, "Invalid Auth Code"),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid JWT Token"),


    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request"),
    BAD_CREDENTIALS(HttpStatus.BAD_REQUEST, "Bad Credentials"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User Not Found"),

    EMAIL_ALREADY_IN_USE(HttpStatus.CONFLICT, "Email Already In Use"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");

    private final HttpStatus httpStatus;
    private final String message;
}
