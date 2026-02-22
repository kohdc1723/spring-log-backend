package org.example.springlogbackend.handler;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springlogbackend.dto.ErrorCode;
import org.example.springlogbackend.util.SecurityResponseUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull AccessDeniedException accessDeniedException
    ) throws IOException {
        log.warn("Access denied: {} {} {}",
                request.getMethod(),
                request.getRequestURI(),
                accessDeniedException.getMessage());
        SecurityResponseUtil.sendError(response, ErrorCode.FORBIDDEN, objectMapper);
    }
}
