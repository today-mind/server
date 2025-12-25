package com.example.todaymindserver.domain.token;

import org.springframework.http.HttpStatus;

import com.example.todaymindserver.domain.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenErrorCode implements ErrorCode {

    INVALID_ACCESS_TOKEN("Access Token이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),

    NOT_FOUND_REFRESH_TOKEN("Refresh Token을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_REFRESH_TOKEN("Refresh Token이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);

    private final String message;
    private final HttpStatus httpStatus;

}
