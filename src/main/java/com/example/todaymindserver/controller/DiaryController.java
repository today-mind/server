package com.example.todaymindserver.controller;

import com.example.todaymindserver.common.response.ApiResponse;
import com.example.todaymindserver.dto.request.DiaryRequestDto;
import com.example.todaymindserver.dto.response.DiaryCalendarResponseDto;
import com.example.todaymindserver.dto.response.DiaryDetailResponseDto;
import com.example.todaymindserver.dto.response.DiaryResponseDto;
import com.example.todaymindserver.service.DiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

/**
 * 일기(Diary) 관련 API Controller
 */
@RestController
@RequestMapping("/api/diaries") // 공통 경로 설정
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    /**
     * 1. 일기 작성 API
     */
    @PostMapping
    public ApiResponse<DiaryResponseDto> createDiary(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid DiaryRequestDto request) {

        // 로컬 테스트용 고정 ID (인증 미구현 시)
        Long targetId = (userId == null) ? 1L : userId;

        DiaryResponseDto response = diaryService.createDiary(targetId, request);
        return ApiResponse.success("일기 작성이 완료되었습니다.", response, HttpStatus.CREATED);
    }

    /**
     * 3. 일기 월별 캘린더 모아보기 API
     */
    @GetMapping("/calendar")
    public ApiResponse<DiaryCalendarResponseDto> getCalendar(
            @AuthenticationPrincipal Long userId,
            @RequestParam String yearMonth) {

        Long targetId = (userId == null) ? 1L : userId;
        DiaryCalendarResponseDto response = diaryService.getCalendar(targetId, yearMonth);
        return ApiResponse.success("캘린더 조회가 완료되었습니다.", response);
    }

    /**
     * 특정 일기 상세 조회 API
     * [주의] 변수 경로({diaryId})는 가장 아래에 두는 것이 관례입니다.
     */
    @GetMapping("/{diaryId}")
    public ApiResponse<DiaryDetailResponseDto> getDiaryDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long diaryId) {

        Long targetId = (userId == null) ? 1L : userId;

        DiaryDetailResponseDto response = diaryService.getDiaryDetail(targetId, diaryId);

        return ApiResponse.success("일기 상세 조회가 완료되었습니다.", response);
    }
}