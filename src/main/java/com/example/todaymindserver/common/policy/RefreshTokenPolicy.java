package com.example.todaymindserver.common.policy;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.example.todaymindserver.common.util.JwtUtil;
import com.example.todaymindserver.entity.RefreshToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenPolicy {

    private final JwtUtil jwtUtil;

    /**
     * "이 토큰이 Refresh Token으로서 형식/타입/만료 기준을 만족하는가?"
     */
    public void validateRefreshToken(String token) {
        validateRefreshTokenType(token);
        validateNotExpired(token);
    }

    private void validateRefreshTokenType(String token) {
        String tokenType = jwtUtil.extractTokenType(token);
        if (!jwtUtil.getRefreshTokenTypeValue().equals(tokenType)) {
            log.error("Token Type이 Refresh가 아닙니다. type={}", tokenType);
            throw new IllegalArgumentException("유효한 Refresh Token이 아닙니다.");
        }
    }

    private void validateNotExpired(String token) {
        LocalDateTime expiration = jwtUtil.extractExpiration(token);
        if (expiration.isBefore(LocalDateTime.now())) {
            log.error("만료된 Refresh Token입니다. exp={}", expiration);
            throw new IllegalArgumentException("유효한 Refresh Token이 아닙니다.");
        }
    }

    /**
     * DB에 저장된 RefreshToken과 요청 토큰이 정책상 동일한지 검사
     */
    public void validateStoredTokenMatch(String inputToken, RefreshToken stored) {
        if (!inputToken.equals(stored.getToken())) {
            log.error("Refresh Token 불일치: 요청={}, 저장된={}", inputToken, stored.getToken());
            throw new IllegalArgumentException("유효한 Refresh Token이 아닙니다.");
        }
    }
}

