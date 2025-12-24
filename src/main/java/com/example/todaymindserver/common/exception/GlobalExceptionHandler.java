package com.example.todaymindserver.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.todaymindserver.domain.BusinessException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 예상 못한 모든 서버 오류 */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {

        log.error("오류 내용: {}", e.getMessage(), e);

        ErrorResponse response = ErrorResponse.builder()
            .status(e.getErrorCode().getHttpStatus().value())
            .code(e.getErrorCode().getHttpStatus().name())
            .message(e.getErrorCode().getMessage())
            .build();

        return ResponseEntity.status(e.getErrorCode().getHttpStatus().value()).body(response);
    }

    /** 예상 못한 모든 서버 오류 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {

        log.error("오류 내용: {}", e.getMessage(), e);

        ErrorResponse response = ErrorResponse.builder()
            .status(500)
            .code("INTERNAL_SERVER_ERROR")
            .message("알 수 없는 오류입니다.")
            .build();

        return ResponseEntity.status(500).body(response);
    }
}
