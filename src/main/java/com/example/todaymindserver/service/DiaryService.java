package com.example.todaymindserver.service;

import com.example.todaymindserver.dto.request.DiaryRequestDto;
import com.example.todaymindserver.dto.response.DiaryResponseDto; // Import 변경
import com.example.todaymindserver.entity.Diary;
import com.example.todaymindserver.entity.User;
import com.example.todaymindserver.repository.DiaryRepository;
import com.example.todaymindserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 일기(Diary) 관련 비즈니스 로직 처리 Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    // private final AiService aiService; // <--- AiService 의존성 제거 (리뷰 미반영)

    /**
     * 일기 작성 처리 및 AI 답장 생성 트리거
     * <p>why: 일기 내용을 DB에 저장하고, 저장 성공 시 AI 답장 생성을 위한 비동기 로직을 호출합니다.</p>
     *
     * @param userId 인증된 사용자 ID
     * @param request 일기 작성 요청 DTO
     * @return 저장된 일기의 고유 ID DTO
     */
    @Transactional
    public DiaryResponseDto createDiary(Long userId, DiaryRequestDto request) { // <--- 반환 타입 변경

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

        // 3. [TODO] AI 엔진으로 일기 내용 전송 (비동기 트리거) - 주석 처리 유지
        // why: 1번 기능 명세에 따라, 최종 저장 시 일기 내용은 AI 엔진으로 전송되어 답장을 생성해야 합니다.
        // aiService.triggerAiResponseGeneration(diary); // <--- AI Service 호출 제거
        log.info("일기 작성 완료 및 AI 트리거 대기: DiaryId={}", diary.getDiaryId());

        // 4. Response DTO 반환
        return DiaryResponseDto.builder()
                .diaryId(diary.getDiaryId())
                .build();
    }
}