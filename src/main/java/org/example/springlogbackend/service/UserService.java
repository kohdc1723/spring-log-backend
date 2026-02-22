package org.example.springlogbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.dto.ErrorCode;
import org.example.springlogbackend.dto.auth.signup.SignUpRequest;
import org.example.springlogbackend.dto.auth.signup.SignUpResponse;
import org.example.springlogbackend.dto.user.UserResponse;
import org.example.springlogbackend.entity.Account;
import org.example.springlogbackend.entity.ProviderType;
import org.example.springlogbackend.entity.User;
import org.example.springlogbackend.entity.UserRoleType;
import org.example.springlogbackend.exception.BusinessException;
import org.example.springlogbackend.repository.AccountRepository;
import org.example.springlogbackend.repository.RefreshTokenRepository;
import org.example.springlogbackend.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional(readOnly = true)
    public boolean exists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        if (exists(signUpRequest.email())) {
            throw new BusinessException(
                    ErrorCode.EMAIL_ALREADY_IN_USE,
                    "User already exists with %s"
                            .formatted(signUpRequest.email())
            );
        }

        User user = User.builder()
                .email(signUpRequest.email())
                .role(UserRoleType.USER)
                .build();

        User savedUser = userRepository.save(user);

        Account account = Account.builder()
                .user(user)
                .provider(ProviderType.LOCAL)
                .password(passwordEncoder.encode(signUpRequest.password()))
                .build();

        Account savedAccount = accountRepository.save(account);

        return SignUpResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .provider(savedAccount.getProvider())
                .createdAt(savedAccount.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public UserResponse readUser(String userId) {
        User user = userRepository.findByIdAndDeleted(userId, false)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND,
                        "User not found with id: %s and not deleted"
                                .formatted(userId)));

        return UserResponse.builder()
                .userId(userId)
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.findByIdAndDeleted(userId, false)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND,
                        "User not found with id: %s and not deleted"
                                .formatted(userId)));
        user.delete();

        accountRepository.deleteAllByUserId(userId);
        refreshTokenRepository.deleteAllByUserId(userId);
    }
}
