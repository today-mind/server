package com.example.todaymindserver.common.exception;

import lombok.Getter;

/**
 * 프로젝트 전역에서 사용할 커스텀 예외 클래스
 * <p>why: 발생한 에러의 정보를 ErrorCode 객체에 담아서 전달합니다.</p>
 */
@Getter // <-- 이게 있어야 getErrorCode() 빨간 줄이 사라져요!
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}