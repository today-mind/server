package com.example.todaymindserver.common.response.dto;

import com.example.todaymindserver.domain.user.MbtiType;
import com.example.todaymindserver.domain.user.ToneType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponseDto {
    private final String nickname;
    private final String email;
    private final MbtiType mbtiType;
    private final ToneType toneType;
    private boolean isAppLockEnabled;
}