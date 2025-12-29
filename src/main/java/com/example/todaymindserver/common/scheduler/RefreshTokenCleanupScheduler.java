package com.example.todaymindserver.common.scheduler;

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
    @Scheduled(cron = "0 10 18 * * *")
    public void cleanupExpiredRefreshTokens() {
        try {
            log.info("RefreshToken 만료 정리 스케줄 시작");
            refreshTokenService.deleteExpiredRefreshTokens();
        } catch (Exception e) {
            log.error("RefreshToken 만료 정리 스케줄 실패", e);
        }
    }
}
