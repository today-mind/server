package com.example.todaymindserver.service;

import com.example.todaymindserver.common.response.dto.ProfileResponseDto;
import com.example.todaymindserver.domain.diary.Diary;
import com.example.todaymindserver.dto.request.AiSettingsRequestDto;
import com.example.todaymindserver.dto.request.NicknameRequestDto;
import com.example.todaymindserver.dto.response.NicknameResponseDto;
import com.example.todaymindserver.domain.BusinessException;
import com.example.todaymindserver.domain.user.User;
import com.example.todaymindserver.domain.user.UserErrorCode;
import com.example.todaymindserver.repository.DiaryRepository;
import com.example.todaymindserver.repository.RefreshTokenRepository;
import com.example.todaymindserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final DiaryRepository diaryRepository;

    /** 1. 닉네임 설정 (PATCH /api/nickname) */
    @Transactional
    public NicknameResponseDto setupNickname(Long userId, NicknameRequestDto request) {
        User user = getUser(userId);

        // 닉네임 업데이트
        user.updateNickname(request.getNickname());

        // 엔티티 대신 DTO 반환
        return NicknameResponseDto.builder()
                .nickname(user.getNickName())
                .build();
    }

    /** 2. 마이페이지 프로필 정보 조회 (GET /api/my-page/profile) */
    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(Long userId) { // 반환 타입을 User -> ProfileResponseDto로 변경
        User user = getUser(userId);

        // 엔티티(User)를 응답 DTO로 변환하여 반환
        return ProfileResponseDto.builder()
                .nickname(user.getNickName())
                .email(user.getEmail())
                .mbtiType(user.getMbtiType())
                .toneType(user.getToneType())
                .isAppLockEnabled(user.getPassword() != null)
                .build();
    }

    /**
     * [Branch 5 최종] AI 설정 변경
     * 용준님의 피드백에 따라 엔티티의 개별 업데이트 메서드를 사용하여
     * 서비스 레이어의 삼항 연산자를 제거한 깨끗한 로직입니다.
     */
    @Transactional
    public void updateAiSettings(Long userId, AiSettingsRequestDto request) {
        User user = getUser(userId);

        // 1. 성향(MBTI) 업데이트 (값이 있을 때만)
        if (request.getPersonalityType() != null) {
            user.updateMbtiType(request.getPersonalityType());
        }

        // 2. 말투(Tone) 업데이트 (값이 있을 때만)
        if (request.getSpeechStyle() != null) {
            user.updateToneType(request.getSpeechStyle());
        }
    }

    @Transactional
    public void logout(Long userId) {
        getUser(userId);

        refreshTokenRepository.deleteByUserId(userId);
    }

    @Transactional
    public void delete(Long userId) {
        User user = getUser(userId);

        // 연관된 코드 삭제
        refreshTokenRepository.deleteByUserId(userId);
        diaryRepository.findAllByUser_UserIdAndDeletedAtIsNull(userId).forEach(Diary::softDelete);
        user.softDelete();
    }

    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> {
                log.warn("사용자가 존재하지 않습니다. userId={}", userId);
                return new BusinessException(UserErrorCode.USER_NOT_FOUND);
            });
    }
}