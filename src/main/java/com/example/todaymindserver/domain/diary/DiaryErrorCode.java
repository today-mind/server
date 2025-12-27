package com.example.todaymindserver.domain.diary;

import org.springframework.http.HttpStatus;

import com.example.todaymindserver.domain.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiaryErrorCode implements ErrorCode {

    DIARY_NOT_FOUND("일기를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DIARY_ALREADY_EXISTS_TODAY(        "오늘은 이미 일기를 작성했습니다.", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus httpStatus;

}
