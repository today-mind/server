package com.example.todaymindserver.domain.user;

import org.springframework.http.HttpStatus;

import com.example.todaymindserver.domain.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NICKNAME_REQUIRED("닉네임 설정이 필요합니다.", HttpStatus.CONFLICT),
    REJOIN_COOLDOWN_ACTIVE("회원 탈퇴 후 48시간이 지나야 다시 가입할 수 있습니다. ", HttpStatus.FORBIDDEN);

    private final String message;
    private final HttpStatus httpStatus;

}