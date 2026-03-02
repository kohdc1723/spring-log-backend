package org.example.springlogbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.dto.ErrorCode;
import org.example.springlogbackend.dto.account.AccountResponse;
import org.example.springlogbackend.dto.auth.UserPrincipal;
import org.example.springlogbackend.dto.auth.signup.SignUpRequest;
import org.example.springlogbackend.dto.auth.signup.SignUpResponse;
import org.example.springlogbackend.dto.refreshtoken.RefreshTokenResponse;
import org.example.springlogbackend.dto.auth.AuthUserResponse;
import org.example.springlogbackend.dto.user.UserResponse;
import org.example.springlogbackend.entity.Account;
import org.example.springlogbackend.entity.ProviderType;
import org.example.springlogbackend.entity.User;
import org.example.springlogbackend.entity.UserRoleType;
import org.example.springlogbackend.exception.BusinessException;
import org.example.springlogbackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        Optional<User> existingUser = userRepository.findByEmailAndDeleted(signUpRequest.email(), false);

        if (existingUser.isPresent()) {
            boolean hasLocalAccount = existingUser.get().getAccounts().stream()
                    .anyMatch(acc -> acc.getProvider() == ProviderType.LOCAL);

            if (hasLocalAccount) {
                throw new BusinessException(
                        ErrorCode.EMAIL_ALREADY_IN_USE,
                        "User with email(%s) and provider(%s) already exists"
                                .formatted(signUpRequest.email(), ProviderType.LOCAL.name()));
            }
        }

        User user = existingUser.orElseGet(() -> User.builder()
                .email(signUpRequest.email())
                .role(UserRoleType.USER)
                .build());

        Account account = Account.builder()
                .user(user)
                .provider(ProviderType.LOCAL)
                .password(passwordEncoder.encode(signUpRequest.password()))
                .build();

        user.getAccounts().add(account);

        userRepository.save(user);

        return SignUpResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .provider(account.getProvider())
                .createdAt(account.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public AuthUserResponse getAuthUser(UserPrincipal userPrincipal) {
        String userId = userPrincipal.userId();
        ProviderType provider = userPrincipal.provider();

        User user = userRepository.findByIdAndDeleted(userId, false)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND,
                        "User(%s) not found".formatted(userId)));

        Account account = user.getAccounts().stream()
                .filter(acc -> acc.getProvider() == provider)
                .findFirst()
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND,
                        "Account not found with user(%s) and provider(%s)".formatted(userId, provider.name())));

        return AuthUserResponse.builder()
                .email(user.getEmail())
                .role(user.getRole())
                .provider(account.getProvider())
                .profileImageUrl(account.getProfileImageUrl())
                .build();
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsers() {
        return userRepository.findAllByDeleted(false).stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .createdAt(user.getCreatedAt())
                        .updatedAt(user.getUpdatedAt())
                        .accounts(user.getAccounts().stream()
                                .map(acc -> AccountResponse.builder()
                                        .id(acc.getId())
                                        .provider(acc.getProvider())
                                        .providerId(acc.getProviderId())
                                        .profileImageUrl(acc.getProfileImageUrl())
                                        .createdAt(acc.getCreatedAt())
                                        .updatedAt(acc.getUpdatedAt())
                                        .build()
                                )
                                .toList()
                        )
                        .refreshTokens(user.getRefreshTokens().stream()
                                .map(rt -> RefreshTokenResponse.builder()
                                        .token(rt.getToken())
                                        .build()
                                )
                                .toList()
                        )
                        .build()
                )
                .toList();
    }

    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.findByIdAndDeleted(userId, false)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND,
                        "User not found with id: %s and not deleted".formatted(userId)));

        user.delete();
    }
}
