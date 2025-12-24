package com.example.todaymindserver.controller;

import com.example.todaymindserver.common.response.ApiResponse;
import com.example.todaymindserver.dto.request.DiaryRequestDto;
import com.example.todaymindserver.dto.response.DiaryCalendarResponseDto;
import com.example.todaymindserver.dto.response.DiaryDetailResponseDto;
import com.example.todaymindserver.dto.response.DiaryResponseDto;
import com.example.todaymindserver.service.DiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

/**
 * 일기(Diary) 관련 API 컨트롤러
 * [리뷰 반영]
 * 1. @AuthenticationPrincipal Object -> Long userId로 변경 (팀 표준 준수)
 * 2. 불필요한 타입 체크 및 헬퍼 메서드(getSafeUserId) 삭제
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    /**
     * 일기 작성 API
     */
    @PostMapping("/diaries")
    public ApiResponse<DiaryResponseDto> createDiary(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid DiaryRequestDto request) {

        log.info("일기 작성 요청 - 유저 ID: {}", userId);
        DiaryResponseDto response = diaryService.createDiary(userId, request);
        return ApiResponse.success("일기 작성이 완료되었습니다.", response, HttpStatus.CREATED);
    }

    /**
     * 일기 월별 캘린더 모아보기 API
     */
    @GetMapping("/diaries/calendar")
    public ApiResponse<DiaryCalendarResponseDto> getCalendarDiaries(
            @AuthenticationPrincipal Long userId,
            @RequestParam("yearMonth") @DateTimeFormat(pattern = "yyyyMM") YearMonth yearMonth) {

        log.info("캘린더 조회 요청 - 유저 ID: {}, 날짜: {}", userId, yearMonth);

        DiaryCalendarResponseDto response = diaryService.getCalendarDiaries(userId, yearMonth);
        return ApiResponse.success("캘린더 일기 목록 조회가 완료되었습니다.", response);
    }

    /**
     * 특정 일기 상세 조회 API
     */
    @GetMapping("/diaries/{diaryId}")
    public ApiResponse<DiaryDetailResponseDto> getDiaryDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable("diaryId") Long diaryId) {

        log.info("일기 상세 조회 요청 - 유저 ID: {}, 일기 ID: {}", userId, diaryId);
        DiaryDetailResponseDto response = diaryService.getDiaryDetail(userId, diaryId);
        return ApiResponse.success("일기 상세 조회가 완료되었습니다.", response);
    }
}