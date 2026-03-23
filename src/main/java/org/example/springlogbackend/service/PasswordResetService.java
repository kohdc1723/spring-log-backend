package org.example.springlogbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.dto.ErrorCode;
import org.example.springlogbackend.entity.Account;
import org.example.springlogbackend.entity.PasswordResetToken;
import org.example.springlogbackend.entity.ProviderType;
import org.example.springlogbackend.entity.User;
import org.example.springlogbackend.exception.BusinessException;
import org.example.springlogbackend.repository.AccountRepository;
import org.example.springlogbackend.repository.PasswordResetTokenRepository;
import org.example.springlogbackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void sendPasswordResetEmail(String email) {
        userRepository.findByEmailAndDeleted(email, false)
                .filter(user ->
                        accountRepository.findByUserIdAndProvider(user.getId(), ProviderType.LOCAL).isPresent())
                .ifPresent(this::processPasswordResetEmail);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_PASSWORD_RESET_TOKEN));

        if (passwordResetToken.getExpiresAt().isBefore(Instant.now())) {
            throw new BusinessException(ErrorCode.PASSWORD_RESET_TOKEN_EXPIRED);
        }

        User user = passwordResetToken.getUser();
        Account account = accountRepository.findByUserIdAndProvider(user.getId(), ProviderType.LOCAL)
                .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST));

        account.updatePassword(passwordEncoder.encode(newPassword));
        passwordResetTokenRepository.delete(passwordResetToken);
    }

    private void processPasswordResetEmail(User user) {
        passwordResetTokenRepository.findFirstByUserOrderByCreatedAtDesc(user)
                .ifPresent(token -> {
                    if (token.getCreatedAt()
                            .isAfter(Instant.now().minus(1, ChronoUnit.MINUTES))) {
                        throw new BusinessException(ErrorCode.TOO_MANY_REQUESTS);
                    }
                });

        passwordResetTokenRepository.deleteByUserId(user.getId());

        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .build();

        passwordResetTokenRepository.save(passwordResetToken);
        emailService.sendPasswordResetEmail(user.getEmail(), passwordResetToken.getToken());
    }
}
