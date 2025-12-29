package com.example.todaymindserver.common.util;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.todaymindserver.domain.BusinessException;
import com.example.todaymindserver.domain.token.TokenErrorCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final Pattern BEARER_PATTERN = Pattern.compile("^Bearer\\s+[A-Za-z0-9-_.]+$");

    public boolean isValidBearerToken(String authorizationHeader) {
        return authorizationHeader != null && BEARER_PATTERN.matcher(authorizationHeader).matches();
    }

    public String extractToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(BEARER_PREFIX.length()); // "Bearer " 제거 후 반환
        }

        log.warn("토큰이 type이 Bearer가 아닙니다.");
        throw new BusinessException(TokenErrorCode.INVALID_ACCESS_TOKEN);
    }
}

