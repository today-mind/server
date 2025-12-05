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

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK)
                .message(message)
                .data(data)
                .build();
    }

    public static ApiResponse<?> success(String message) {
        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .message(message)
                .data(null)
                .build();
    }
}