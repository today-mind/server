package com.example.todaymindserver.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private final int status;
    private final String message;
    private final T data; // 응답 데이터 (Generic Type)

    @Builder
    public ApiResponse(HttpStatus status, String message, T data) {
        this.status = status.value();
        this.message = message;
        this.data = data;
    }

    // 1. [추가된 핵심 메서드] HTTP Status를 직접 지정할 수 있게 함 (201 Created 등 처리)
    public static <T> ApiResponse<T> success(String message, T data, HttpStatus status) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }

    // 2. [기존 메서드 개선] Status를 지정하지 않으면 200 OK로 처리 (편의성 유지)
    public static <T> ApiResponse<T> success(String message, T data) {
        // 새로운 메서드를 호출하며 기본값으로 200 OK를 전달
        return success(message, data, HttpStatus.OK);
    }

    // 3. [기존 메서드 유지] 데이터 없이 메시지만 전달할 때 (200 OK로 처리)
    public static ApiResponse<?> success(String message) {
        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .message(message)
                .data(null)
                .build();
    }
}