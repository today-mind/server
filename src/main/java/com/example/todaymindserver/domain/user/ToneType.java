package com.example.todaymindserver.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 */
@Getter
@RequiredArgsConstructor
public enum ToneType {
    HONORIFIC("존댓말"),
    INFORMAL("반말");

    private final String korean;

    public static ToneType fromString(String tone) {
        if (tone == null || tone.isBlank()) {
            return HONORIFIC;
        }

        String upperTone = tone.toUpperCase();
        try {
            return ToneType.valueOf(upperTone);
        } catch (IllegalArgumentException e) {
            if ("BANMAL".equals(upperTone)) {
                return INFORMAL;
            }
            return HONORIFIC;
        }
    }
}