package com.example.todaymindserver.common.security;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private SecurityServletErrorCode resolveErrorCode(AuthenticationException authException, HttpServletRequest req) {
        Throwable cause = authException.getCause();

        // 1) cause 없음
        if (cause == null) {
            if (authException instanceof BadCredentialsException) {
                log.warn("JWT 인증 실패(BadCredentialsException, cause 없음): message={}", authException.getMessage());
                return SecurityServletErrorCode.INVALID_JWT;
            }
            log.warn("인증 실패(원인 알 수 없음): url={}, message={}", req.getRequestURI(), authException.getMessage());
            return SecurityServletErrorCode.UNAUTHORIZED;
        }

        // 2) jjwt 관련 예외 매핑
        if (cause instanceof SecurityException) {
            log.warn("Access Token이 잘못된 key로 서명 검증을 시도하였습니다: {}", cause.getMessage());
            return SecurityServletErrorCode.INVALID_JWT_SIGNATURE;
        } else if (cause instanceof MalformedJwtException) {
            log.warn("Access Token이 JWT 구조를 만족하지 않습니다: {}", cause.getMessage());
            return SecurityServletErrorCode.INVALID_JWT_FORMAT;
        } else if (cause instanceof ExpiredJwtException) {
            log.warn("Access Token이 만료되었습니다: {}", cause.getMessage());
            return SecurityServletErrorCode.EXPIRED_JWT_TOKEN;
        } else if (cause instanceof UnsupportedJwtException) {
            log.warn("서버에서 지원하지 않은 방식으로 서명된 JWT 토큰입니다.: {}", cause.getMessage());
            return SecurityServletErrorCode.UNSUPPORTED_JWT;
        } else {
            log.warn("Access Token 인증 과정 중 알 수 없는 서버 오류: {}", cause.getMessage(), cause);
            return SecurityServletErrorCode.INTERNAL_SERVER_ERROR;
        }
    }

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException {

        SecurityServletErrorCode errorCode = resolveErrorCode(authException, request);

        SecurityResponseWriter.writeJsonErrorResponse(response, errorCode);
    }
}
