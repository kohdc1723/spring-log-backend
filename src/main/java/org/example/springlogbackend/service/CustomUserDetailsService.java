package org.example.springlogbackend.service;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.dto.auth.CustomUserDetails;
import org.example.springlogbackend.entity.Account;
import org.example.springlogbackend.entity.ProviderType;
import org.example.springlogbackend.entity.User;
import org.example.springlogbackend.repository.AccountRepository;
import org.example.springlogbackend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(@Nonnull String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndDeleted(username, false)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with email: %s".formatted(username)));

        Account account = accountRepository.findByUserIdAndProvider(user.getId(), ProviderType.LOCAL)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Local account not found with userId: %s".formatted(user.getId())));

        return CustomUserDetails.builder()
                .user(user)
                .account(account)
                .build();
    }
}
