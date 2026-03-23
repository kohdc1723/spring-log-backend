package org.example.springlogbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request"),
    BAD_CREDENTIALS(HttpStatus.BAD_REQUEST, "Bad Credentials"),
    INVALID_VERIFICATION_TOKEN(HttpStatus.BAD_REQUEST, "Invalid Verification Token"),
    VERIFICATION_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "Verification Token Expired"),
    INVALID_PASSWORD_RESET_TOKEN(HttpStatus.BAD_REQUEST, "Invalid Password Reset Token"),
    PASSWORD_RESET_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "Password Reset Token Expired"),
    // 401
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    INVALID_AUTH_CODE(HttpStatus.UNAUTHORIZED, "Invalid Auth Code"),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid JWT Token"),
    // 403
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),
    EMAIL_NOT_VERIFIED(HttpStatus.FORBIDDEN, "Email Not Verified"),
    // 404
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User Not Found"),
    // 405
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed"),
    // 409
    EMAIL_ALREADY_IN_USE(HttpStatus.CONFLICT, "Email Already In Use"),
    // 429
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests"),
    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");

    private final HttpStatus httpStatus;
    private final String message;
}
