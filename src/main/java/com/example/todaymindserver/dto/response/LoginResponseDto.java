package com.example.todaymindserver.dto.response;

public record LoginResponseDto(
    String accessToken,
    String refreshToken,
    String email,
    String nickName
) {}
