package com.example.todaymindserver.common.response.dto;

import com.example.todaymindserver.common.util.MbtiType;
import com.example.todaymindserver.common.util.ToneType;
import com.example.todaymindserver.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 프로필 조회 시 반환할 데이터 구조입니다.
 * 필드 타입을 Enum으로 유지하여 타입 안정성을 확보했습니다.
 */
@Getter
@Builder
@AllArgsConstructor
public class ProfileResponseDto {
    private final String nickname;
    private final String email;
    private final MbtiType mbtiType;
    private final ToneType toneType;

    /**
     * User 엔티티를 ProfileResponseDto로 변환합니다.
     * @param user 조회된 사용자 엔티티
     * @return 프로필 응답 DTO
     */
    public static ProfileResponseDto from(User user) {
        return ProfileResponseDto.builder()
                .nickname(user.getNickName())
                .email(user.getEmail())
                .mbtiType(user.getMbtiType())
                .toneType(user.getToneType())
                .build();
    }
}