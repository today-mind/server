package com.example.todaymindserver.common.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 카카오 사용자 정보 응답 DTO
 * [수정] 닉네임 수집을 위해 kakaoAccount 내부에 profile 정보를 추가했습니다.
 */
public record KakaoUserResponse(
        Long id,

        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
) {
    /**
     * 카카오 계정 정보
     */
    public record KakaoAccount(
            String email,
            Profile profile // 닉네임이 담긴 프로필 객체 추가
    ) {
        /**
         * 프로필 정보
         */
        public record Profile(
                String nickname // 실제 닉네임 필드
        ) {}
    }
}