package com.example.todaymindserver.common.util;

import org.springframework.security.core.GrantedAuthority;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum Role implements GrantedAuthority {
    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
