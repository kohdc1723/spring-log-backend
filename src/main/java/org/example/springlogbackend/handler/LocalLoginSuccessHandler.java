package org.example.springlogbackend.handler;

import jakarta.annotation.Nonnull;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.dto.CustomUserDetails;
import org.example.springlogbackend.service.JwtService;
import org.example.springlogbackend.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LocalLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String userId = userDetails.getUserId();
        String email = userDetails.getUsername();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        String accessToken = jwtUtil.createAccessToken(userId, email, role);
        String refreshToken = jwtUtil.createRefreshToken(userId, email, role);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/auth")
                .sameSite("Strict")
                .maxAge(Duration.ofDays(14))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        jwtService.addRefreshToken(refreshToken);
        Map<String, String> accessTokenResponse = Map.of("accessToken", accessToken);
        objectMapper.writeValue(response.getWriter(), accessTokenResponse);
    }
}
