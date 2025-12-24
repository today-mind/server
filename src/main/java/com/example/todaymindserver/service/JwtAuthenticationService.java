package com.example.todaymindserver.service;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.todaymindserver.common.util.JwtProvider;
import com.example.todaymindserver.common.util.Role;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtAuthenticationService {

    private final JwtProvider jwtProvider;

    public Authentication getAuthentication(String token) {
        Claims claims = jwtProvider.parseClaims(token);

        String type = jwtProvider.extractTokenType(claims);
        if (!jwtProvider.getAccessTokenTypeValue().equals(type)) {
            log.error("Access token type이 아닙니다.");
            throw new RuntimeException("유효하지 않는 토큰입니다.");
        }

        Long userId = jwtProvider.extractUserId(claims);

        return new UsernamePasswordAuthenticationToken(
            userId,
            null,
            List.of(Role.USER)
        );
    }
}
