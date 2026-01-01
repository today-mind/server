package com.example.todaymindserver.common.policy;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.example.todaymindserver.common.util.JwtProvider;
import com.example.todaymindserver.domain.BusinessException;
import com.example.todaymindserver.domain.token.RefreshToken;
import com.example.todaymindserver.domain.token.TokenErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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

        // 1️⃣ null / blank
        if (token == null || token.isBlank()) {
            log.warn("Refresh Token이 null 또는 blank 입니다.");
            throw new BusinessException(TokenErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 2️⃣ 파싱 (라이브러리 예외 → 도메인 예외)
        Claims claims = parseClaimsSafely(token);

        // 3️⃣ 타입 검증
        validateRefreshTokenType(claims);
    }

    /**
     * JWT 파싱 (라이브러리 예외 → 도메인 예외로 변환)
     */
    private Claims parseClaimsSafely(String token) {
        try {
            return jwtProvider.parseClaims(token);

        } catch (ExpiredJwtException e) {
            Date exp = e.getClaims() != null ? e.getClaims().getExpiration() : null;
            log.warn("만료된 Refresh Token입니다. exp={}", exp);
            throw new BusinessException(TokenErrorCode.EXPIRED_REFRESH_TOKEN);

        } catch (JwtException | IllegalArgumentException e) {
            // MalformedJwtException, UnsupportedJwtException 포함
            log.warn("유효하지 않은 Refresh Token입니다. message={}", e.getMessage());
            throw new BusinessException(TokenErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    private void validateRefreshTokenType(Claims claims) {
        String tokenType = jwtProvider.extractTokenType(claims);

        if (!jwtProvider.getRefreshTokenTypeValue().equals(tokenType)) {
            log.warn("Token Type이 Refresh가 아닙니다. 요청 token type={}", tokenType);
            throw new BusinessException(TokenErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    /**
     * DB에 저장된 RefreshToken과 요청 토큰이 정책상 동일한지 검사
     */
    public void validateStoredTokenMatch(String inputToken, RefreshToken stored) {
        if (!inputToken.equals(stored.getToken())) {
            log.warn("저장된 refresh token과 불일치합니다. userId={}", stored.getUserId());
            throw new BusinessException(TokenErrorCode.INVALID_REFRESH_TOKEN);
        }
    }
}

