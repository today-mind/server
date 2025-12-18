package com.example.todaymindserver.service;

import com.example.todaymindserver.dto.request.DiaryRequestDto;
import com.example.todaymindserver.dto.response.DiaryCalendarResponseDto; // Import 추가
import com.example.todaymindserver.dto.response.DiaryDetailResponseDto; // Import 추가
import com.example.todaymindserver.dto.response.DiaryResponseDto;
import com.example.todaymindserver.entity.Diary;
import com.example.todaymindserver.entity.User;
import com.example.todaymindserver.repository.DiaryRepository;
import com.example.todaymindserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    // private final AiService aiService; // 1번 PR 리뷰 반영 전이므로 일단 주석 처리 유지

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. (ID: " + userId + ")"));

        // 2. Diary 엔티티 생성 및 저장
        Diary diary = Diary.create(
                user,
                request.getContent(),
                request.getEmotionType()
        );
        diaryRepository.save(diary);

        // 3. [TODO] AI 엔진으로 일기 내용 전송 (비동기 트리거)
        log.info("일기 작성 완료 및 AI 트리거 대기: DiaryId={}", diary.getDiaryId());

        return DiaryResponseDto.builder()
                .diaryId(diary.getDiaryId())
                .build();
    }

    // --- 3번 기능 구현 ---

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
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. (ID: " + userId + ")"));

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
        Diary foundDiary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("일기를 찾을 수 없습니다."));

        if (!foundDiary.getUser().getUserId().equals(userId)) {
            // 여기에 CustomAccessDeniedException 같은 권한 예외를 던져야 합니다.
            throw new IllegalArgumentException("해당 일기에 접근할 권한이 없습니다.");
        }


        // 2. [TODO] AI 답변 조회 (현재는 AI 답변 엔티티가 없으므로 더미 데이터 사용)
        String aiReply = "AI 답변 준비 중입니다."; // 현재는 더미 데이터

        // 3. DTO 반환
        return DiaryDetailResponseDto.from(foundDiary, aiReply);
    }
}