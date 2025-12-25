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
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers(HttpMethod.POST,
                        "/api/auth/test-token",
                    "/oauth/**",
                    "/auth/token/refresh"
                ).permitAll()
                .requestMatchers("/api/diaries/**").hasRole(Role.USER.name())
                .requestMatchers("/api/users/**").hasRole(Role.USER.name())
                .requestMatchers("/api/**").authenticated()

                .anyRequest().denyAll()
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
