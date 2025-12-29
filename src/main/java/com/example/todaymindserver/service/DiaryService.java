package com.example.todaymindserver.service;

import com.example.todaymindserver.common.event.dto.EmpatheticResponseEvent;
import com.example.todaymindserver.domain.user.EmotionType;
import com.example.todaymindserver.dto.request.DiaryRequestDto;
import com.example.todaymindserver.dto.response.DiaryCalendarResponseDto; // Import 추가
import com.example.todaymindserver.dto.response.DiaryDetailResponseDto; // Import 추가
import com.example.todaymindserver.dto.response.DiaryResponseDto;
import com.example.todaymindserver.domain.BusinessException;
import com.example.todaymindserver.domain.diary.Diary;
import com.example.todaymindserver.domain.diary.DiaryErrorCode;
import com.example.todaymindserver.domain.user.User;
import com.example.todaymindserver.dto.response.EmotionReportResponseDto;
import com.example.todaymindserver.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 일기(Diary) 관련 비즈니스 로직 처리 Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserService userService;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 일기 작성 처리 및 AI 답장 생성 트리거 (기존 1번 기능)
     * <p>why: 일기 내용을 DB에 저장하고, 저장 성공 시 AI 답장 생성을 위한 비동기 로직을 호출합니다.</p>
     *
     * @param userId 인증된 사용자 ID
     * @param request 일기 작성 요청 DTO
     * @return 저장된 일기의 고유 ID DTO
     */
    @Transactional
    public DiaryResponseDto createDiary(Long userId, DiaryRequestDto request) {

        // 1. User 엔티티 조회
        User user = userService.getUser(userId);

        user.validateUserNickNameExists();

        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        if(diaryRepository.existsByUserAndCreatedAtBetween(user, start, end)) {
            log.warn("일기는 하루에 한 번만 작성할 수 있습니다. userId={}", userId);
            throw new BusinessException(DiaryErrorCode.DIARY_ALREADY_EXISTS_TODAY);
        }

        // 2. Diary 엔티티 생성 및 저장
        Diary diary = Diary.create(
                user,
                request.getContent(),
                request.getEmotionType()
        );
        diaryRepository.save(diary);

        // 3. Ai 응답 이벤트 발행
        applicationEventPublisher.publishEvent(
            new EmpatheticResponseEvent(
                diary.getDiaryId(),
                diary.getContent(),
                diary.getEmotionType(),
                user.getUserId(),
                user.getNickName(),
                user.getMbtiType(),
                user.getToneType()
            )
        );

        return DiaryResponseDto.builder()
                .diaryId(diary.getDiaryId())
                .build();
    }


    /**
     * 일기 월별 캘린더 모아보기
     * <p>why: 사용자가 캘린더에서 일기 작성 여부를 한눈에 확인하고, 날짜 선택을 할 수 있도록 데이터를 제공합니다.</p>
     *
     * @param userId 인증된 사용자 ID
     * @param yearMonth 조회할 연도와 월 (예: 2025-10)
     * @return 해당 월에 작성된 일기 목록 (날짜, 감정 유형 포함)
     */
    @Transactional(readOnly = true)
    public DiaryCalendarResponseDto getCalendarDiaries(Long userId, YearMonth yearMonth) {

        // 1. 해당 월의 시작일과 종료일 계산
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        LocalDateTime startOfMonth = startDate.atStartOfDay();
        LocalDateTime endOfMonth = endDate.atTime(23, 59, 59);

        // 2. User 엔티티 조회
        User user = userService.getUser(userId);

        // 3. DB에서 해당 월의 일기 목록 조회 (특정 사용자의 일기만)
        List<Diary> diaries = diaryRepository.findByUserAndCreatedAtBetweenOrderByCreatedAtDesc(
                user,
                startOfMonth,
                endOfMonth
        );

        // 4. DTO로 변환
        List<DiaryCalendarResponseDto.DiaryCalendarItem> items = diaries.stream()
                .map(DiaryCalendarResponseDto.DiaryCalendarItem::from)
                .collect(Collectors.toList());

        return DiaryCalendarResponseDto.builder()
                .diaries(items)
                .build();
    }

    /**
     * 특정 일기 상세 조회 (캘린더에서 날짜 선택 시)
     * <p>why: 캘린더 뷰에서 특정 날짜를 선택하면, 해당 일기의 원문과 AI 답변을 제공합니다.</p>
     *
     * @param userId 인증된 사용자 ID
     * @param diaryId 조회할 일기 ID
     * @return 일기 상세 정보 DTO
     */
    @Transactional(readOnly = true)
    public DiaryDetailResponseDto getDiaryDetail(Long userId, Long diaryId) {

        // 1. Diary 조회 및 권한 검증 (해당 일기가 이 유저의 것인지 확인)
        Diary diary = getDiary(userId, diaryId);

        // 2. 본인 일기 검증
        diary.validateOwner(userId);

        // 3. DTO 반환
        return DiaryDetailResponseDto.from(diary);
    }

    /**
     * [Step 3] 감정 통계 리포트 생성 로직
     * 1. 유저 존재 여부 확인
     * 2. 요청된 년/월의 시작일과 종료일(다음달 1일) 계산
     * 3. 해당 기간의 일기들을 DB에서 조회
     * 4. Java Stream을 이용해 감정별로 그룹화 및 카운팅
     */
    public EmotionReportResponseDto getEmotionReport(Long userId, int year, int month) {
        // 1. 유저 조회 (없으면 예외 발생)
        User user = userService.getUser(userId);

        // 2. 해당 월의 시작일 (예: 12월 1일 00:00:00)
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        // 다음 달의 시작일 (예: 1월 1일 00:00:00) - 쿼리 조건에서 < end로 사용
        LocalDateTime startOfNextMonth = startOfMonth.plusMonths(1);

        // 3. Repository를 통해 기간 내 일기 목록 조회
        List<Diary> diaries = diaryRepository.findAllByUserAndCreatedAtBetween(user, startOfMonth, startOfNextMonth);

        // 4. 감정별 그룹화 및 개수 집계
        // diary.getEmotionType().name() -> "HAPPY", "SAD" 등의 문자열을 키로 사용합니다.
        Map<EmotionType, Long> emotionCounts = diaries.stream()
                .collect(Collectors.groupingBy(
                        diary -> EmotionType.valueOf(diary.getEmotionType().name()),
                        Collectors.counting()
                ));

        // 5. 최종 DTO 생성 및 반환
        return EmotionReportResponseDto.builder()
                .year(year)
                .month(month)
                .totalCount(diaries.size())
                .emotionCounts(emotionCounts)
                .build();
    }

    public Diary getDiary(Long userId, Long diaryId) {
        return diaryRepository.findById(diaryId)
            .orElseThrow(() -> {
                log.warn("일기가 존재하지 않습니다. userId={}, diaryId={}", userId, diaryId);
                return new BusinessException(DiaryErrorCode.DIARY_NOT_FOUND);
            });

    }

    @Transactional(readOnly = true)
    public void getAiResponse(Long userId, Long diaryId) {
        User user = userService.getUser(userId);

        Diary diary = diaryRepository.findById(diaryId)
            .orElseThrow(() -> {
                log.warn("일기를 찾을 수 없습니다. userId={}, diaryId={}", userId, diaryId);
                return new BusinessException(DiaryErrorCode.DIARY_NOT_FOUND);
            });

        diary.validateRetryAllowed();

        // 3. Ai 응답 이벤트 발행
        applicationEventPublisher.publishEvent(
            new EmpatheticResponseEvent(
                diary.getDiaryId(),
                diary.getContent(),
                diary.getEmotionType(),
                user.getUserId(),
                user.getNickName(),
                user.getMbtiType(),
                user.getToneType()
            )
        );
    }

    @Transactional
    public void deleteDiary(Long userId, Long diaryId) {
        // 1. User 엔티티 조회
        userService.getUser(userId);

        // 2. 일기 조회
        Diary diary = getDiary(userId, diaryId);

        // 3. 본인 일기 검증
        diary.validateOwner(userId);

        // 4. 일기 삭제
        diaryRepository.delete(diary);
    }
}