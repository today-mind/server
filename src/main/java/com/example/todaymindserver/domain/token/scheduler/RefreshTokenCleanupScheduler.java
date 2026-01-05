package com.example.todaymindserver.domain.token.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.todaymindserver.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenCleanupScheduler {

    private final RefreshTokenService refreshTokenService;

    /**
     * 매일 새벽 3시 만료 RefreshToken 정리
     */
    @Scheduled(cron = "${scheduler.refresh-token.cleanup.cron}")
    public void cleanupExpiredRefreshTokens() {
        try {
            log.info("[Scheduler][RefreshTokenCleanup] 시작");
            refreshTokenService.deleteExpiredRefreshTokens();
        } catch (Exception e) {
            log.error(
                "[Scheduler][RefreshTokenCleanup] 비재시도 예외 발생",
                e
            );
        }
    }
}
