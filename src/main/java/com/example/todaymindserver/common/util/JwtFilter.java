package com.example.todaymindserver.common.util;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.todaymindserver.service.JwtAuthenticationService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtAuthenticationService authService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
        @Nonnull HttpServletRequest request,
        @Nonnull HttpServletResponse response,
        @Nonnull FilterChain chain
    ) throws ServletException, IOException {

        try {
            String token = resolveToken(request);

            // 토큰 없으면 익명으로 진행
            if (token != null) {
                SecurityContextHolder.getContext().setAuthentication(
                    authService.getAuthentication(token)
                );
            }

        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | SignatureException e) {
            throw new BadCredentialsException("Invalid JWT", e);
        } catch (Exception e) {
            // 👉 정말 예상 못 한 오류
            log.error("[JWT FILTER ERROR] 알 수 없는 인증 처리 오류", e);
            throw new BadCredentialsException("Authentication processing failed", e);
        }
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");

        if(!jwtUtil.isValidBearerToken(bearer)) {
            return null;
        }

        return jwtUtil.extractToken(bearer);
    }
}
