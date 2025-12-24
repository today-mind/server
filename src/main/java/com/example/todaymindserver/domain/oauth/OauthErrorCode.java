package com.example.todaymindserver.domain.oauth;

import org.springframework.http.HttpStatus;

import com.example.todaymindserver.domain.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OauthErrorCode implements ErrorCode {

    GOOGLE_USER_INFO_NOT_FOUND("구글 사용자 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    KAKAO_USER_INFO_NOT_FOUND("카카오 사용자 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus httpStatus;

}
