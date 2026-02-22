package org.example.springlogbackend.handler;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springlogbackend.dto.ErrorCode;
import org.example.springlogbackend.util.SecurityResponseUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        log.warn("Unauthorized: {} {} {}",
                request.getMethod(),
                request.getRequestURI(),
                authException.getMessage());
        SecurityResponseUtil.sendError(response, ErrorCode.UNAUTHORIZED, objectMapper);
    }
}
