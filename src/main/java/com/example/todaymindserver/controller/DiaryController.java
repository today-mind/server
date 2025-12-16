package com.example.todaymindserver.controller;

import com.example.todaymindserver.common.response.ApiResponse;
import com.example.todaymindserver.dto.request.DiaryRequestDto;
import com.example.todaymindserver.service.DiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 일기(Diary) 관련 API Controller
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    /**
     * 1. 일기 작성 API
     *
     * @param userId 인증된 사용자 ID
     * @param request 일기 작성 요청 DTO
     * @return 생성된 일기의 ID
     */
    @PostMapping("/diaries") // REST API 컨벤션: 복수형 명사
    public ApiResponse<Long> createDiary(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid DiaryRequestDto request) {

        Long diaryId = diaryService.createDiary(userId, request);

        // REST API 컨벤션: 리소스 생성 성공 시 201 Created 반환
        return ApiResponse.success("일기 작성이 완료되었습니다.", diaryId, HttpStatus.CREATED);
    }
}