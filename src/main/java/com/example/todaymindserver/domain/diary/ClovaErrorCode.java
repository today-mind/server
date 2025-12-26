package com.example.todaymindserver.domain.diary;

import org.springframework.http.HttpStatus;

import com.example.todaymindserver.domain.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClovaErrorCode implements ErrorCode {

    EMPATHETIC_RESPONSE_STATUS_CONFLICT("AI 응답 상태가 PENDING일 때만 완료 처리할 수 있습니다.", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus httpStatus;
}
