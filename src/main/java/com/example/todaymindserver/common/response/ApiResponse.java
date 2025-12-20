package com.example.todaymindserver.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private final int status;
    private final String code; // 추가
    private final String message;
    private final T data;

    @Builder
    public ApiResponse(HttpStatus status, String code, String message, T data) {
        this.status = status.value();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK)
                .code("SUCCESS")
                .message(message)
                .data(data)
                .build();
    }

    public static ApiResponse<Void> error(String code, String message, HttpStatus status) {
        return ApiResponse.<Void>builder()
                .status(status)
                .code(code)
                .message(message)
                .data(null)
                .build();
    }
}