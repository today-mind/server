package com.example.todaymindserver.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum OauthProviderType {
    GOOGLE,
    KAKAO;

    public static OauthProviderType from(String provider) {
        try {
            return OauthProviderType.valueOf(provider.toUpperCase());
        } catch (Exception e) {
            log.error("Oauth provider Type 오류 입니다: {}", e.getMessage());
            throw new IllegalArgumentException("지원하지 않는 OAuth Provider: " + provider);
        }
    }
}
