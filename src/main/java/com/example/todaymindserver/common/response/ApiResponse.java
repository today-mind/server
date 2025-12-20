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

    // 성공 응답 (기존과 동일)
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

    // [수정] 에러 응답 - 이제 null이 아니라 제대로 된 객체를 반환
    public static ApiResponse<Void> error(String code, String message) {
        return ApiResponse.<Void>builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("[" + code + "] " + message)
                .data(null)
                .build();
    }
}