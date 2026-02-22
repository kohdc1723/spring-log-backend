package org.example.springlogbackend.handler;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.util.SecurityResponseUtil;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nullable Authentication authentication
    ) throws IOException {
        SecurityResponseUtil.sendSuccess(response, null, objectMapper);
    }
}
