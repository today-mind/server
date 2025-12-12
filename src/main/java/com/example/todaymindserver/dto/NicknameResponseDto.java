package com.example.todaymindserver.dto;

import lombok.Builder;
import lombok.Getter;

// NicknameResponseDto.java
@Getter
@Builder
public class NicknameResponseDto {
    private final String nickname;
}