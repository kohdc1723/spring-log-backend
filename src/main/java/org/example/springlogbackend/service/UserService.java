package org.example.springlogbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.dto.CustomUserDetails;
import org.example.springlogbackend.dto.request.auth.SignUpRequest;
import org.example.springlogbackend.dto.response.auth.SignUpResponse;
import org.example.springlogbackend.entity.Account;
import org.example.springlogbackend.entity.ProviderType;
import org.example.springlogbackend.entity.User;
import org.example.springlogbackend.entity.UserRoleType;
import org.example.springlogbackend.repository.AccountRepository;
import org.example.springlogbackend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public boolean exists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        if (exists(signUpRequest.email())) {
            throw new IllegalArgumentException("User already exists with " + signUpRequest.email());
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

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndDeleted(username, false)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Account account = accountRepository.findByUserIdAndProvider(user.getId(), ProviderType.LOCAL)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return CustomUserDetails.builder()
                .user(user)
                .password(account.getPassword())
                .build();
    }
}
