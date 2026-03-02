package org.example.springlogbackend.handler;

import jakarta.annotation.Nonnull;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.dto.ErrorCode;
import org.example.springlogbackend.util.SecurityResponseUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LocalLoginFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull AuthenticationException exception
    ) throws IOException {
        if (exception instanceof BadCredentialsException) {
            SecurityResponseUtil.sendError(response, ErrorCode.BAD_CREDENTIALS, objectMapper);
        } else if (exception instanceof UsernameNotFoundException) {
            SecurityResponseUtil.sendError(response, ErrorCode.USER_NOT_FOUND, objectMapper);
        } else {
            SecurityResponseUtil.sendError(response, ErrorCode.UNAUTHORIZED, objectMapper);
        }
    }
}
