package com.example.todaymindserver.common.exception;

import com.example.todaymindserver.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 처리기입니다.
 * 모든 예외를 잡아 공통 응답 형식(ApiResponse)으로 변환합니다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 로직 상의 예외를 처리 (400, 404 등)
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("BusinessException 발생: [{} - {}]", errorCode.getCode(), errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getStatus())
                .body((ApiResponse<Void>) ApiResponse.error(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage()));
    }

    /**
     * 그 외 예상치 못한 모든 예외를 처리 (500)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        // [중요] 500 에러가 발생할 때 콘솔에 정확한 에러 내용을 찍어줍니다.
        // 인텔리제이 Console 탭을 확인해서 어떤 에러인지 파악하세요.
        log.error("Internal Server Error: ", e);

        return ResponseEntity
                .status(500)
                .body(ApiResponse.error(500, "COMMON_ERROR", "서버 내부 오류가 발생했습니다."));
    }
}