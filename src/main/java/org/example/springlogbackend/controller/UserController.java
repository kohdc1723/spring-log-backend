package org.example.springlogbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.dto.ApiResponse;
import org.example.springlogbackend.dto.user.UserResponse;
import org.example.springlogbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMeApi(@AuthenticationPrincipal String userId) {
        UserResponse userResponse = userService.readUser(userId);

        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("#id == authentication.name or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUserApi(@PathVariable String id) {
        userService.deleteUser(id);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
