package com.example.todaymindserver.controller;

import com.example.todaymindserver.common.response.ApiResponse;
import com.example.todaymindserver.dto.request.DiaryRequestDto;
import com.example.todaymindserver.dto.response.DiaryCalendarResponseDto; // Import 추가
import com.example.todaymindserver.dto.response.DiaryDetailResponseDto; // Import 추가
import com.example.todaymindserver.dto.response.DiaryResponseDto;
import com.example.todaymindserver.service.DiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth; // Import 추가

/**
 * 일기(Diary) 관련 API Controller
 * <p>why: REST API 컨벤션에 따라 URI는 복수형 명사(/diaries)를 사용합니다.</p>
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    // 1. 일기 작성 API (기존 1번 기능)
    @PostMapping("/diaries")
    public ApiResponse<DiaryResponseDto> createDiary(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid DiaryRequestDto request) {

        DiaryResponseDto response = diaryService.createDiary(userId, request);
        return ApiResponse.success("일기 작성이 완료되었습니다.", response, HttpStatus.CREATED);
    }

    // --- 3번 기능 구현 ---

    /**
     * 일기 월별 캘린더 모아보기 API
     * <p>why: 캘린더 기능은 월별로 리소스를 조회하므로, 쿼리 파라미터로 yearMonth를 받습니다.</p>
     *
     * @param userId 인증된 사용자 ID
     * @param yearMonth 조회할 연도와 월 (예: 2025-10)
     * @return 해당 월에 작성된 일기 목록 DTO
     */
    @GetMapping("/diaries/calendar")
    public ApiResponse<DiaryCalendarResponseDto> getCalendarDiaries(
            @AuthenticationPrincipal Long userId,
            @RequestParam("yearMonth") YearMonth yearMonth) { // Spring 3.x에서 YearMonth 타입 바인딩 지원

        DiaryCalendarResponseDto response = diaryService.getCalendarDiaries(userId, yearMonth);
        return ApiResponse.success("캘린더 일기 목록 조회가 완료되었습니다.", response);
    }

    /**
     * 특정 일기 상세 조회 API (캘린더에서 날짜 선택 시)
     * <p>why: 특정 ID의 일기 리소스를 조회하므로 Path Variable을 사용합니다.</p>
     *
     * @param userId 인증된 사용자 ID
     * @param diaryId 조회할 일기 ID
     * @return 일기 상세 정보 DTO
     */
    @GetMapping("/diaries/{diaryId}")
    public ApiResponse<DiaryDetailResponseDto> getDiaryDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable("diaryId") Long diaryId) {

        DiaryDetailResponseDto response = diaryService.getDiaryDetail(userId, diaryId);
        return ApiResponse.success("일기 상세 조회가 완료되었습니다.", response);
    }
}