package com.example.todaymindserver.common.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private final int status;   // 404, 400 등
    private final String code;     // U001, D001 등
    private final String message;  // 에러 메시지
}