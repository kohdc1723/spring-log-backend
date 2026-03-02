package org.example.springlogbackend.filter;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.dto.ErrorCode;
import org.example.springlogbackend.dto.auth.UserPrincipal;
import org.example.springlogbackend.entity.ProviderType;
import org.example.springlogbackend.util.JwtTokenType;
import org.example.springlogbackend.util.JwtUtil;
import org.example.springlogbackend.util.SecurityResponseUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorization.split(" ")[1];

        if (jwtUtil.isValid(accessToken, JwtTokenType.ACCESS)) {
            String userId = jwtUtil.getSubject(accessToken);
            String provider = jwtUtil.getProvider(accessToken);
            UserPrincipal userPrincipal = UserPrincipal.builder()
                    .userId(userId)
                    .provider(ProviderType.valueOf(provider))
                    .build();

            String role = jwtUtil.getRole(accessToken);
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

            Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);
        } else {
            SecurityResponseUtil.sendError(response, ErrorCode.INVALID_JWT_TOKEN, objectMapper);
        }
    }
}
