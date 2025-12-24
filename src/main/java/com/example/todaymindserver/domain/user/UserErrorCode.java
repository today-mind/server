package com.example.todaymindserver.domain.user;

import org.springframework.http.HttpStatus;

import com.example.todaymindserver.domain.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus httpStatus;

}
