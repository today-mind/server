package com.example.todaymindserver.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum Role {
    USER,
    ADMIN;

    public static Role from(String role) {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (Exception e) {
            log.error("사용자 역할 오류 입니다: {}", e.getMessage());
            throw new IllegalArgumentException("지원하지 않는 사용자 역할입니다: " + role);
        }
    }
}
