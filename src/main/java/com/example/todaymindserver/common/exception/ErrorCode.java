package com.example.todaymindserver.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 전역 에러 코드 정의 (심플 버전)
 * 비즈니스 전용 코드(code)를 제거하고 HTTP 상태와 메시지만 관리합니다.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

    // App Lock
    INVALID_APP_PASSWORD(HttpStatus.BAD_REQUEST, "잠금 비밀번호가 일치하지 않습니다."),
    APP_LOCK_NOT_SET(HttpStatus.NOT_FOUND, "설정된 잠금 비밀번호가 없습니다.");

    private final HttpStatus status;
    private final String message;
}