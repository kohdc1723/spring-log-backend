package org.example.springlogbackend.config;

import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.repository.EmailVerificationTokenRepository;
import org.example.springlogbackend.repository.RefreshTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class ScheduleConfig {
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Scheduled(cron = "0 0 3 * * *")
    public void refreshTokenTtlSchedule() {
        Instant cutoff = Instant.now().minus(8, ChronoUnit.DAYS);
        refreshTokenRepository.deleteByCreatedAtBefore(cutoff);
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void emailVerificationTokenTtlSchedule() {
        emailVerificationTokenRepository.deleteByExpiresAtBefore(Instant.now());
    }
}
