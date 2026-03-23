package org.example.springlogbackend.handler;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.dto.auth.response.AccessTokenResponse;
import org.example.springlogbackend.dto.auth.CustomUserDetails;
import org.example.springlogbackend.entity.ProviderType;
import org.example.springlogbackend.service.JwtService;
import org.example.springlogbackend.util.CookieUtil;
import org.example.springlogbackend.util.JwtTokenType;
import org.example.springlogbackend.util.JwtUtil;
import org.example.springlogbackend.util.SecurityResponseUtil;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LocalLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new AuthenticationServiceException("Unexpected principal type");
        }

        String userId = userDetails.getUserId();
        String email = userDetails.getUsername();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        String accessToken = jwtUtil.createToken(userId, ProviderType.LOCAL, email, role, JwtTokenType.ACCESS);
        String refreshToken = jwtUtil.createToken(userId, ProviderType.LOCAL, email, role, JwtTokenType.REFRESH);

        jwtService.addRefreshToken(refreshToken);
        cookieUtil.setRefreshToken(response, refreshToken);

        AccessTokenResponse accessTokenResponse = new AccessTokenResponse(accessToken);
        SecurityResponseUtil.sendSuccess(response, accessTokenResponse, objectMapper);
    }
}
