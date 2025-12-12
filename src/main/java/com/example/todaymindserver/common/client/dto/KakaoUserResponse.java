package com.example.todaymindserver.common.client.dto;

public record KakaoUserResponse(
    Long id,
    KakaoAccount kakaoAccount
) {

    public record KakaoAccount(
        String email
    ) {}
}
