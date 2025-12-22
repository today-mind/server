package com.example.todaymindserver.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * AI 답장 톤앤매너 타입
 */
@Getter
@RequiredArgsConstructor
public enum ToneType {
    HONORIFIC("존댓말"),
    FRIENDLY("친근한 말투"),
    CASUAL("반말");

    private final String description;
}