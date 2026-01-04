package com.example.todaymindserver.dto.response;

import com.example.todaymindserver.domain.diary_write_status.DiaryWriteStatusType;

public record DiaryWriteStatusResponseDto(
    DiaryWriteStatusType diaryWriteStatusType,
    Long diaryId
) {}
