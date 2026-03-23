package org.example.springlogbackend.handler;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.dto.ErrorCode;
import org.example.springlogbackend.util.SecurityResponseUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
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
        switch (exception) {
            case BadCredentialsException badCredentialsException ->
                    SecurityResponseUtil.sendError(response, ErrorCode.BAD_CREDENTIALS, objectMapper);
            case UsernameNotFoundException usernameNotFoundException ->
                    SecurityResponseUtil.sendError(response, ErrorCode.BAD_CREDENTIALS, objectMapper);
            case DisabledException disabledException ->
                    SecurityResponseUtil.sendError(response, ErrorCode.EMAIL_NOT_VERIFIED, objectMapper);
            default -> SecurityResponseUtil.sendError(response, ErrorCode.UNAUTHORIZED, objectMapper);
        }
    }
}
