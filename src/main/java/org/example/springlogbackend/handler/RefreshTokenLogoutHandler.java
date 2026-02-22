package org.example.springlogbackend.handler;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.service.JwtService;
import org.example.springlogbackend.util.CookieUtil;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenLogoutHandler implements LogoutHandler {
    private final JwtService jwtService;
    private final CookieUtil cookieUtil;

    @Override
    public void logout(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nullable Authentication authentication
    ) {
        String refreshToken = cookieUtil.getRefreshToken(request);

        if (refreshToken == null) return;

        jwtService.deleteRefreshToken(refreshToken);
        cookieUtil.removeRefreshToken(response);
    }
}
