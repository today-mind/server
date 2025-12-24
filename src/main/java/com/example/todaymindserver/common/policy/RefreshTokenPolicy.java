package com.example.todaymindserver.common.policy;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.example.todaymindserver.common.util.JwtProvider;
import com.example.todaymindserver.domain.BusinessException;
import com.example.todaymindserver.domain.token.RefreshToken;
import com.example.todaymindserver.domain.token.TokenErrorCode;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenPolicy {

    private final JwtProvider jwtProvider;

    /**
     * "이 토큰이 Refresh Token으로서 형식/타입/만료 기준을 만족하는가?"
     */
    public void validateRefreshToken(String token) {
        Claims claims = jwtProvider.parseClaims(token);
        validateRefreshTokenType(claims);
        validateNotExpired(claims);
    }

    private void validateRefreshTokenType(Claims claims) {
        String tokenType = jwtProvider.extractTokenType(claims);
        if (!jwtProvider.getRefreshTokenTypeValue().equals(tokenType)) {
            log.error("Token Type이 Refresh가 아닙니다. type={}", tokenType);
            throw new BusinessException(TokenErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    private void validateNotExpired(Claims claims) {
        LocalDateTime expiration = jwtProvider.extractExpiration(claims);
        if (expiration.isBefore(LocalDateTime.now())) {
            log.error("만료된 Refresh Token입니다. exp={}", expiration);
            throw new BusinessException(TokenErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    /**
     * DB에 저장된 RefreshToken과 요청 토큰이 정책상 동일한지 검사
     */
    public void validateStoredTokenMatch(String inputToken, RefreshToken stored) {
        if (!inputToken.equals(stored.getToken())) {
            log.error("Refresh Token 불일치: 요청={}, 저장된={}", inputToken, stored.getToken());
            throw new BusinessException(TokenErrorCode.INVALID_REFRESH_TOKEN);
        }
    }
}

