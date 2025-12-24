package com.example.todaymindserver.common.client.dto;

/**
 * 구글 API 응답 DTO
 * 닉네임 수집을 위해 name 필드를 추가했습니다.
 */
public record GoogleUserResponse(
        String sub,
        String email,
        String name
) {
}