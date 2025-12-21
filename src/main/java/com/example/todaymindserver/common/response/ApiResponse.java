package com.example.todaymindserver.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private final int status;
    private final String code;
    private final String message;
    private final T data;

    @Builder
    public ApiResponse(HttpStatus status, String code, String message, T data) {
        this.status = status.value();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * [추가] 성공 응답 (메시지, 데이터, 커스텀 상태 코드)
     * DiaryController에서 HttpStatus.CREATED를 보낼 수 있게 해줍니다.
     */
    public static <T> ApiResponse<T> success(String message, T data, HttpStatus status) {
        return ApiResponse.<T>builder()
                .status(status)
                .code("SUCCESS") // 성공 시 기본 코드
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 성공 응답 (기본 200 OK)
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return success(message, data, HttpStatus.OK);
    }

    /**
     * 에러 응답
     */
    public static ApiResponse<Void> error(String code, String message, HttpStatus status) {
        return ApiResponse.<Void>builder()
                .status(status)
                .code(code)
                .message(message)
                .data(null)
                .build();
    }
}