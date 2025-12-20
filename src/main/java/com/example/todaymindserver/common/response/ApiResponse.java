package com.example.todaymindserver.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private final int status;
    private final String message;
    private final T data;

    @Builder
    public ApiResponse(HttpStatus status, String message, T data) {
        this.status = status.value();
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String message, T data, HttpStatus status) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return success(message, data, HttpStatus.OK);
    }

    //
    public static ApiResponse<Void> error(String message, HttpStatus status) {
        return ApiResponse.<Void>builder()
                .status(status)
                .message(message)
                .data(null)
                .build();
    }
}