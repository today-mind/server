package com.example.todaymindserver.dto.response;

import com.example.todaymindserver.domain.diary.DiaryWriteStatusType;

public record DiaryWriteStatusResponseDto(
    DiaryWriteStatusType diaryWriteStatusType,
    Long diaryId
) {}
