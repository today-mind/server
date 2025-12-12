package com.example.todaymindserver.common.response.dto;

import com.example.todaymindserver.common.util.MbtiType;
import com.example.todaymindserver.common.util.ToneType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponseDto {
    private final String nickname;
    private final MbtiType mbtiType;
    private final ToneType toneType;
    // ... 필요한 다른 프로필 정보 추가
}