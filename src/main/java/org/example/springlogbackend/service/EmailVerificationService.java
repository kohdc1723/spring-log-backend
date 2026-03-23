package org.example.springlogbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.dto.ErrorCode;
import org.example.springlogbackend.entity.EmailVerificationToken;
import org.example.springlogbackend.entity.User;
import org.example.springlogbackend.exception.BusinessException;
import org.example.springlogbackend.repository.EmailVerificationTokenRepository;
import org.example.springlogbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Transactional
    public void verifyEmail(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST));

        if (verificationToken.getExpiresAt().isBefore(Instant.now())) {
            throw new BusinessException(ErrorCode.VERIFICATION_TOKEN_EXPIRED);
        }

        User user = verificationToken.getUser();
        user.verifyEmail();

        emailVerificationTokenRepository.save(verificationToken);
    }

    @Transactional
    public void sendVerificationEmail(User user) {
        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiresAt(Instant.now().plus(24, ChronoUnit.HOURS))
                .build();

        emailVerificationTokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(user.getEmail(), verificationToken.getToken());
    }

    @Transactional
    public void resendVerificationEmail(String email) {
        User user = userRepository.findByEmailAndDeleted(email, false)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (user.isEmailVerified()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        emailVerificationTokenRepository.findFirstByUserOrderByCreatedAtDesc(user)
                .ifPresent(token -> {
                    if (token.getCreatedAt().isAfter(Instant.now().minus(1, ChronoUnit.MINUTES))) {
                        throw new BusinessException(ErrorCode.TOO_MANY_REQUESTS);
                    }
                });

        emailVerificationTokenRepository.deleteByUserId(user.getId());
        sendVerificationEmail(user);
    }
}
