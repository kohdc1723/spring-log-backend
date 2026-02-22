package org.example.springlogbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.dto.auth.AccessTokenResponse;
import org.example.springlogbackend.dto.auth.oauth2.TokensDto;
import org.example.springlogbackend.dto.auth.oauth2.TokenRequest;
import org.example.springlogbackend.dto.auth.oauth2.TokenResponse;
import org.example.springlogbackend.dto.auth.signup.SignUpRequest;
import org.example.springlogbackend.dto.ApiResponse;
import org.example.springlogbackend.dto.auth.signup.SignUpResponse;
import org.example.springlogbackend.service.JwtService;
import org.example.springlogbackend.service.UserService;
import org.example.springlogbackend.util.CookieUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final CookieUtil cookieUtil;

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUpApi(
            @RequestBody @Valid SignUpRequest signUpRequest
    ) {
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(signUpResponse));
    }

    @PostMapping("/token")
    public ResponseEntity<ApiResponse<TokenResponse>> tokenApi(
            @RequestBody @Valid TokenRequest tokenRequest,
            HttpServletResponse response
    ) {
        TokensDto tokens = jwtService.generateToken(tokenRequest.code());

        cookieUtil.setRefreshToken(response, tokens.refreshToken());

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(tokens.accessToken())
                .build();

        return ResponseEntity.ok(ApiResponse.success(tokenResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AccessTokenResponse>> refreshApi(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        String refreshToken = cookieUtil.getRefreshToken(request);

        TokensDto tokens = jwtService.refresh(refreshToken);

        cookieUtil.setRefreshToken(response, tokens.refreshToken());

        AccessTokenResponse accessTokenResponse = new AccessTokenResponse(tokens.accessToken());

        return ResponseEntity.ok(ApiResponse.success(accessTokenResponse));
    }
}
