package org.example.springlogbackend.config;

import lombok.RequiredArgsConstructor;
import org.example.springlogbackend.repository.RefreshTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ScheduleConfig {
    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 3 * * *")
    public void refreshTokenTtlSchedule() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(8);
        refreshTokenRepository.deleteByCreatedAtBefore(cutoff);
    }
}
