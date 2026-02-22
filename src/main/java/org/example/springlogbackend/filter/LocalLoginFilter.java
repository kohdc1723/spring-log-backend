package org.example.springlogbackend.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springlogbackend.dto.auth.login.LoginRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class LocalLoginFilter extends AbstractAuthenticationProcessingFilter {
    private static final RequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/api/v1/auth/login");
    private final ObjectMapper objectMapper;

    public LocalLoginFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.objectMapper = objectMapper;
    }

    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

            UsernamePasswordAuthenticationToken authToken =
                    UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.email(), loginRequest.password());

            return getAuthenticationManager().authenticate(authToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Failed to authenticate login request");
        }
    }
}
