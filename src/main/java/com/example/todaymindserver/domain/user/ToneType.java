package com.example.todaymindserver.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ToneType {
    HONORIFIC("존댓말"), // 존댓말
    BANMAL("반말");     // 반말

    private final String korean;
}