package com.example.todaymindserver.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.todaymindserver.common.security.SecurityUtil;
import com.example.todaymindserver.domain.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    /** enum 매핑 실패 오류 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        Long userId = SecurityUtil.getCurrentUserIdOrNull();
        log.warn("ENUM 매핑 오류. userId={}, 상세 메시지={}", userId, e.getMessage());

        ErrorResponse response = ErrorResponse.builder()
            .status(400)
            .code("BAD_REQUEST")
            .message("잘못된 요청 값 입니다.")
            .build();

        return ResponseEntity.status(400).body(response);
    }

    /** DTO 필드 검증 실패 오류 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        Long userId = SecurityUtil.getCurrentUserIdOrNull();
        log.warn("Request Body - Dto 매핑 오류. userId={}, 상세 메시지={}", userId, e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());

        ErrorResponse response = ErrorResponse.builder()
            .status(400)
            .code("BAD_REQUEST")
            .message(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage())
            .build();

        return ResponseEntity.status(400).body(response);
    }

    /** 파라미터 누락 오류 */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {

        Long userId = SecurityUtil.getCurrentUserIdOrNull();
        log.warn("Request Param - 누락 오류. userId={}, 상세 메시지={}", userId, e.getMessage());

        ErrorResponse response = ErrorResponse.builder()
            .status(400)
            .code("BAD_REQUEST")
            .message("잘못된 요청 값 입니다.")
            .build();

        return ResponseEntity.status(400).body(response);
    }

    /** 비즈니스 오류 오류 */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {

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

        log.error("예상치 못한 오류. 상세 메시지={}", e.getMessage(), e);

        ErrorResponse response = ErrorResponse.builder()
                .status(500)
                .code("INTERNAL_SERVER_ERROR")
                .message("알 수 없는 오류입니다.")
                .build();

        return ResponseEntity.status(500).body(response);
    }
}