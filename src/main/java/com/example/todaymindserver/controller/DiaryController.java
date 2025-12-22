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
 * 일기(Diary) 관련 API Controller
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    /**
     * 인증 정보(Principal)에서 안전하게 UserId를 추출하는 헬퍼 메서드
     * 인증되지 않은 경우(Swagger 등) 테스트를 위해 1L을 반환합니다.
     */
    private Long getSafeUserId(Object principal) {
        if (principal instanceof Long) {
            return (Long) principal;
        }
        if (principal instanceof String && !principal.equals("anonymousUser")) {
            try {
                return Long.parseLong((String) principal);
            } catch (NumberFormatException e) {
                log.warn("Principal 파싱 실패: {}", principal);
            }
        }
        // 로그인하지 않은 경우 기본값 1L 사용 (500 에러 방지)
        log.info("인증 정보가 없어 기본 ID 1L로 진행합니다. (Principal: {})", principal);
        return 1L;
    }

    // 1. 일기 작성 API
    @PostMapping("/diaries")
    public ApiResponse<DiaryResponseDto> createDiary(
            @AuthenticationPrincipal Object principal,
            @RequestBody @Valid DiaryRequestDto request) {

        Long userId = getSafeUserId(principal);
        DiaryResponseDto response = diaryService.createDiary(userId, request);
        return ApiResponse.success("일기 작성이 완료되었습니다.", response, HttpStatus.CREATED);
    }

    // 3. 일기 월별 캘린더 모아보기 API
    @GetMapping("/diaries/calendar")
    public ApiResponse<DiaryCalendarResponseDto> getCalendarDiaries(
            @AuthenticationPrincipal Object principal,
            @RequestParam("yearMonth") @DateTimeFormat(pattern = "yyyyMM") YearMonth yearMonth) {

        Long userId = getSafeUserId(principal);
        log.info("캘린더 조회 요청 - 유저: {}, 날짜: {}", userId, yearMonth);

        DiaryCalendarResponseDto response = diaryService.getCalendarDiaries(userId, yearMonth);
        return ApiResponse.success("캘린더 일기 목록 조회가 완료되었습니다.", response);
    }

    // 3. 특정 일기 상세 조회 API
    @GetMapping("/diaries/{diaryId}")
    public ApiResponse<DiaryDetailResponseDto> getDiaryDetail(
            @AuthenticationPrincipal Object principal,
            @PathVariable("diaryId") Long diaryId) {

        Long userId = getSafeUserId(principal);
        DiaryDetailResponseDto response = diaryService.getDiaryDetail(userId, diaryId);
        return ApiResponse.success("일기 상세 조회가 완료되었습니다.", response);
    }
}