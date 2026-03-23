package org.example.springlogbackend.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.dto.ApiResponse;
import org.example.springlogbackend.dto.user.UserResponse;
import org.example.springlogbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getUsers();

        return ResponseEntity.ok(ApiResponse.success(users));
    }
}
