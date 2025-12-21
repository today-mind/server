package com.example.todaymindserver.service;

import com.example.todaymindserver.common.exception.BusinessException;
import com.example.todaymindserver.common.exception.ErrorCode;
import com.example.todaymindserver.dto.request.DiaryRequestDto;
import com.example.todaymindserver.dto.response.DiaryCalendarResponseDto;
import com.example.todaymindserver.dto.response.DiaryDetailResponseDto;
import com.example.todaymindserver.dto.response.DiaryResponseDto;
import com.example.todaymindserver.entity.Diary;
import com.example.todaymindserver.entity.User;
import com.example.todaymindserver.repository.DiaryRepository;
import com.example.todaymindserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 일기(Diary) 관련 비즈니스 로직 처리 Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;

    /**
     * 일기 작성 처리 (기존 1번 기능)
     */
    @Transactional
    public DiaryResponseDto createDiary(Long userId, DiaryRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Diary diary = Diary.create(user, request.getContent(), request.getEmotionType());
        diaryRepository.save(diary);

        log.info("일기 작성 완료: DiaryId={}", diary.getDiaryId());

        return DiaryResponseDto.builder()
                .diaryId(diary.getDiaryId())
                .build();
    }

/**
     * 일기 월별 캘린더 모아보기 (3번 기능)
     */
    @Transactional(readOnly = true)
    public DiaryCalendarResponseDto getCalendarDiaries(Long userId, YearMonth yearMonth) {
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        List<Diary> diaries = diaryRepository.findByUserUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, start, end);

        List<DiaryCalendarResponseDto.DiaryCalendarItem> items = diaries.stream()
                .map(DiaryCalendarResponseDto.DiaryCalendarItem::from)
                .collect(Collectors.toList());

        return DiaryCalendarResponseDto.builder()
                .diaries(items)
                .build();
    }

    /**
     * 특정 일기 상세 조회
     */
    @Transactional(readOnly = true)
    public DiaryDetailResponseDto getDiaryDetail(Long userId, Long diaryId) {
        Diary foundDiary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));

        if (!foundDiary.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE); // 권한 에러 대용
        }

        return DiaryDetailResponseDto.from(foundDiary, "AI 답변 준비 중입니다.");
    }

    public DiaryCalendarResponseDto getCalendar(Long targetId, String yearMonth) {
        return null;
    }
}