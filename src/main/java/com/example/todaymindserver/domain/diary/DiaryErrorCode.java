package com.example.todaymindserver.domain.diary;

import org.springframework.http.HttpStatus;

import com.example.todaymindserver.domain.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiaryErrorCode implements ErrorCode {

    DIARY_NOT_FOUND("일기를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DIARY_ALREADY_EXISTS_TODAY(        "오늘은 이미 일기를 작성했습니다.", HttpStatus.CONFLICT),
    INVALID_RESPONSE_STATUS_TRANSITION("이미 처리된 AI 응답 상태입니다.", HttpStatus.CONFLICT),
    DIARY_ACCESS_DENIED("일기 접근 권한이 없습니다.", HttpStatus.FORBIDDEN);

    private final String message;
    private final HttpStatus httpStatus;

}
