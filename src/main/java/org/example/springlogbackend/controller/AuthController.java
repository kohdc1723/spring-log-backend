package org.example.springlogbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.dto.request.auth.SignUpRequest;
import org.example.springlogbackend.dto.response.ApiResponse;
import org.example.springlogbackend.dto.response.auth.SignUpResponse;
import org.example.springlogbackend.service.UserService;
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

    @PostMapping(value = "/sign-up")
    public ResponseEntity<ApiResponse<?>> signUpApi(
            @RequestBody @Valid SignUpRequest signUpRequest
    ) {
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(signUpResponse));
    }
}
