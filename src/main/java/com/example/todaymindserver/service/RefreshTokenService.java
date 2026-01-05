package com.example.todaymindserver.service;

import java.time.LocalDateTime;

import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todaymindserver.common.client.slack.SlackNotifierClient;
import com.example.todaymindserver.domain.token.RefreshTokenPolicy;
import com.example.todaymindserver.common.util.JwtProvider;
import com.example.todaymindserver.domain.user.User;
import com.example.todaymindserver.dto.request.RefreshTokenRequestDto;
import com.example.todaymindserver.dto.response.RefreshTokenResponseDto;
import com.example.todaymindserver.domain.BusinessException;
import com.example.todaymindserver.domain.token.RefreshToken;
import com.example.todaymindserver.domain.token.TokenErrorCode;
import com.example.todaymindserver.repository.RefreshTokenRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final SlackNotifierClient slackNotifierClient;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenPolicy refreshTokenPolicy;
    private final JwtProvider jwtProvider;

    @Transactional
    public void issueInitialToken(User user, String refreshToken) {

        if (refreshTokenRepository.existsById(user.getUserId())) {
            return; // 이미 존재하면 생성 안 함
        }

        Claims claims = jwtProvider.parseClaims(refreshToken);
        LocalDateTime expiresAt = jwtProvider.extractExpiration(claims);

        RefreshToken token = RefreshToken.create(
            user.getUserId(),
            refreshToken,
            expiresAt
        );

        refreshTokenRepository.save(token);
    }

    @Transactional(readOnly = true)
    protected RefreshToken getRefreshToken(Long userId) {
        return refreshTokenRepository.findById(userId)
            .orElseThrow(() -> {
                log.warn("refresh token을 가지고 있지 않습니다. userId={}", userId);
                return new BusinessException(TokenErrorCode.NOT_FOUND_REFRESH_TOKEN);
            });
    }

    @Transactional
    public void deleteRefreshToken(Long userId) {
        refreshTokenRepository.deleteById(userId);
    }

    /**
     * Refresh Token 재발급 절차를 처리한다.
     * <ol>
     * <li>전달된 Refresh Token 검증 및 저장된 값과의 일치 여부 확인을 포함한다.</li>
     * <li>검증이 완료되면 새로운 AccessToken과 RefreshToken을 생성한다.</li>
     * <li>Rotation 정책에 따라 Refresh Token을 갱신한다.</li>
     * <li>재발급 흐름 전체를 하나의 트랜잭션으로 보장하여 상태 불일치를 예방한다.</li>
     * </ol>
     */
    @Transactional
    public RefreshTokenResponseDto getNewRefreshTokenAndAccessToken(RefreshTokenRequestDto request) {

        String inputToken = request.refreshToken();

        refreshTokenPolicy.validateRefreshToken(inputToken);

        Long userId = jwtProvider.extractUserId(jwtProvider.parseClaims(inputToken));

        RefreshToken storedRefreshToken = getRefreshToken(userId);

        refreshTokenPolicy.validateStoredTokenMatch(inputToken, storedRefreshToken);

        String accessToken = jwtProvider.createAccessToken(userId);
        String refreshToken = jwtProvider.createRefreshToken(userId);

        // token 업데이트
        storedRefreshToken.updateToken(refreshToken);

        return new RefreshTokenResponseDto(
            accessToken,
            refreshToken
        );
    }

    @Retryable(
        retryFor = { DataAccessException.class },
        maxAttempts = 2, // 최초 1회 + 재시도 1회
        backoff = @Backoff(delay = 1000)
    )
    @Transactional
    public void deleteExpiredRefreshTokens() {
        LocalDateTime now = LocalDateTime.now();
        int deletedCount = refreshTokenRepository.deleteByExpiresAtBefore(now);

        if (deletedCount > 0) {
            log.info("만료 refresh token 삭제 - count={}", deletedCount);
        }
    }

    @Recover
    public void recover(DataAccessException e) {
        log.error(
            "[Scheduler][RefreshTokenCleanup] 재시도 1회 후 최종 실패 - reason={}",
            e.getMessage(),
            e
        );

        slackNotifierClient.sendNotification("""
        [Refresh token 정리 스케줄 실패]
        • 작업: RefreshTokenCleanup
        • 재시도: 1회 후 실패
        • 원인: %s
        • 시각: %s
        """.formatted(
                e.getMessage(),
                LocalDateTime.now()
            )
        );
    }
}
