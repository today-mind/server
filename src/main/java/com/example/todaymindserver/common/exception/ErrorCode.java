package com.example.todaymindserver.common.exception;

import lombok.Getter;

/**
 * 프로젝트 전역에서 사용하는 에러 코드 정의
 */
@Getter
public enum ErrorCode {
    // 공통 에러
    INTERNAL_SERVER_ERROR(500, "COMMON_ERROR", "서버 내부 오류가 발생했습니다."),

    // 유저 관련 에러 (U)
    USER_NOT_FOUND(404, "U001", "사용자를 찾을 수 없습니다."),
    APP_LOCK_NOT_SET(400, "U002", "잠금 설정이 되어 있지 않습니다."),
    INVALID_APP_PASSWORD(400, "U003", "비밀번호가 일치하지 않습니다."),
    NICKNAME_DUPLICATED(400, "U004", "이미 사용 중인 닉네임입니다.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}