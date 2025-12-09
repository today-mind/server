package com.example.todaymindserver.common.util;

import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {

    private static final String CLAIM_TYPE = "type";
    private static final String TYPE_ACCESS = "ACCESS";
    private static final String TYPE_REFRESH = "REFRESH";

    @Value("${jwt.access-token.expire-time}")
    private Duration accessTokenExpireTime;

    @Value("${jwt.refresh-token.expire-time}")
    private Duration refreshTokenExpireTime;

    @Value("${jwt.secret-key}")
    private String secretKey;

    private Key key;
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        if (bytes.length < 32) {
            log.error("JWT 서명키는 최소 256bit 이상이어야 합니다.");
            throw new IllegalArgumentException("유효하지 않은 JWT 서명키 입니다.");
        }
        key = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * Access Token 발급
     * - type = ACCESS 로 구분
     */
    public String createAccessToken(Long userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpireTime.toMillis());

        return Jwts.builder()
            .setSubject(String.valueOf(userId))
            .claim(CLAIM_TYPE, TYPE_ACCESS)
            .setExpiration(expiry)
            .setIssuedAt(now)
            .signWith(key, signatureAlgorithm)
            .compact();
    }

    /**
     * Refresh Token 발급
     * - type = REFRESH 로 구분
     */
    public String createRefreshToken(Long userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenExpireTime.toMillis());

        return Jwts.builder()
            .setSubject(String.valueOf(userId))
            .claim(CLAIM_TYPE, TYPE_REFRESH)
            .setExpiration(expiry)
            .setIssuedAt(now)
            .signWith(key, signatureAlgorithm)
            .compact();
    }

    /**
     * 토큰에서 Claims 추출
     */
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public LocalDateTime extractExpiration(String token) {
        Claims claims = extractClaims(token);
        return LocalDateTime.ofInstant(
            claims .getExpiration().toInstant(),
            ZoneId.systemDefault()
        );
    }

    public Long extractUserId(String token) {
        return Long.valueOf(extractClaims(token).getSubject());
    }

    public String extractTokenType(String token) {
        Claims claims = extractClaims(token);
        return claims.get(CLAIM_TYPE, String.class);
    }

    public String getRefreshTokenTypeValue() {
        return TYPE_REFRESH;
    }
}

