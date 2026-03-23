package org.example.springlogbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.dto.ErrorCode;
import org.example.springlogbackend.dto.auth.*;
import org.example.springlogbackend.dto.auth.oauth2.TokensDto;
import org.example.springlogbackend.dto.auth.oauth2.TokenRequest;
import org.example.springlogbackend.dto.auth.oauth2.TokenResponse;
import org.example.springlogbackend.dto.auth.signup.SignUpRequest;
import org.example.springlogbackend.dto.ApiResponse;
import org.example.springlogbackend.dto.auth.signup.SignUpResponse;
import org.example.springlogbackend.exception.BusinessException;
import org.example.springlogbackend.service.EmailVerificationService;
import org.example.springlogbackend.service.JwtService;
import org.example.springlogbackend.service.PasswordResetService;
import org.example.springlogbackend.service.UserService;
import org.example.springlogbackend.util.CookieUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final EmailVerificationService emailVerificationService;
    private final JwtService jwtService;
    private final CookieUtil cookieUtil;
    private final PasswordResetService passwordResetService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthUserResponse>> getMeApi(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        AuthUserResponse authUserResponse = userService.getAuthUser(userPrincipal);

        return ResponseEntity.ok(ApiResponse.success(authUserResponse));
    }

    @GetMapping("/email-verification")
    public ResponseEntity<ApiResponse<Void>> verifyEmailApi(@RequestParam String token) {
        emailVerificationService.verifyEmail(token);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

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

        if (refreshToken == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        TokensDto tokens = jwtService.refresh(refreshToken);

        cookieUtil.setRefreshToken(response, tokens.refreshToken());

        AccessTokenResponse accessTokenResponse = new AccessTokenResponse(tokens.accessToken());

        return ResponseEntity.ok(ApiResponse.success(accessTokenResponse));
    }

    @PostMapping("/resend-verification-email")
    public ResponseEntity<ApiResponse<Void>> resendEmailVerificationApi(
            @RequestBody @Valid ResendVerificationEmailRequest request
    ) {
        emailVerificationService.resendVerificationEmail(request.email());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPasswordApi(
            @RequestBody @Valid ForgotPasswordRequest request
    ) {
        passwordResetService.sendPasswordResetEmail(request.email());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPasswordApi(
            @RequestBody @Valid ResetPasswordRequest request
    ) {
        passwordResetService.resetPassword(request.token(), request.newPassword());

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
