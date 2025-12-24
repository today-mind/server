package com.example.todaymindserver.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.todaymindserver.common.util.CustomAccessDeniedHandler;
import com.example.todaymindserver.common.util.CustomAuthenticationEntryPoint;
import com.example.todaymindserver.common.util.JwtFilter;
import com.example.todaymindserver.common.util.JwtUtil;
import com.example.todaymindserver.common.util.Role;
import com.example.todaymindserver.service.JwtAuthenticationService;

import lombok.RequiredArgsConstructor;

/**
 * [충돌 해결 완료] 보안 설정 클래스
 * feature/lock-setting과 dev 브랜치의 보안 정책을 통합했습니다.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final CorsConfigurationSource corsConfigurationSource;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. 공통 허용 경로 (Health check, Auth 관련)
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/oauth/**",
                                "/auth/token/refresh"
                        ).permitAll()

                        // 2. 도메인별 권한 설정 (dev 브랜치의 Role.USER 정책 반영)
                        // 신규 기능인 lock-setting, ai-setting은 /api/users/** 경로에 포함되어 있으므로 아래 규칙을 따릅니다.
                        .requestMatchers("/api/diaries/**").hasRole(Role.USER.name())
                        .requestMatchers("/api/users/**").hasRole(Role.USER.name())

                        // 3. 그 외 모든 API는 인증 필요
                        .requestMatchers("/api/**").authenticated()

                        // 4. 나머지 요청은 모두 허용 (Swagger 등)
                        .anyRequest().permitAll()
                )
                .addFilterBefore(new JwtFilter(jwtAuthenticationService, jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(configurer ->
                        configurer
                                .authenticationEntryPoint(authenticationEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler)
                )
                .build();
    }
}