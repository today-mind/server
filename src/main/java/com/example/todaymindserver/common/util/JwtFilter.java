package com.example.todaymindserver.common.util;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.todaymindserver.service.JwtAuthenticationService;

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

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
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
