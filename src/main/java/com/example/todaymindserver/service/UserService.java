package com.example.todaymindserver.service;

import com.example.todaymindserver.common.response.dto.ProfileResponseDto;
import com.example.todaymindserver.domain.user.MbtiType;
import com.example.todaymindserver.domain.user.ToneType;
import com.example.todaymindserver.dto.request.AiSettingsRequestDto;
import com.example.todaymindserver.dto.request.NicknameRequestDto;
import com.example.todaymindserver.dto.response.NicknameResponseDto;
import com.example.todaymindserver.domain.BusinessException;
import com.example.todaymindserver.domain.user.User;
import com.example.todaymindserver.domain.user.UserErrorCode;
import com.example.todaymindserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    // 헬퍼 메서드: 닉네임 중복 체크
    private void checkNicknameDuplication(String nickname) {
        if (userRepository.findByNickName(nickname).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
    }

    /** 1. 닉네임 설정 (PUT /api/nickname) */
    @Transactional
    public NicknameResponseDto setupNickname(Long userId, NicknameRequestDto request) {
        // [TODO] OAuth 구현 후에는 @AuthenticationPrincipal User user를 사용해야 합니다.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        // 닉네임 중복 체크
        checkNicknameDuplication(request.getNickname());

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        // 엔티티(User)를 응답 DTO로 변환하여 반환
        return ProfileResponseDto.builder()
                .nickname(user.getNickName())
                .email(user.getEmail())
                .mbtiType(user.getMbtiType())
                .toneType(user.getToneType())
                .build();
    }

    /* 3. AI 설정 변경 */
    // 이 외에 updatePassword, updateAiSettings 등 나머지 MyPage 로직이 여기에 추가될 수 있습니다.

    /**
     * [Branch 5 수정] AI 설정 변경 (Partial Update 방식)
     * 리액트(프론트)에서 하나만 보낼 경우를 대비해 null 체크 후 기존 값을 유지합니다.
     */
    @Transactional
    public void updateAiSettings(Long userId, AiSettingsRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        // 삼항 연산자를 사용하여 요청값이 null이면 기존(user.get...) 값을 유지하도록 합니다.
        MbtiType finalMbti = (request.getPersonalityType() != null)
                ? request.getPersonalityType()
                : user.getMbtiType();

        ToneType finalTone = (request.getSpeechStyle() != null)
                ? request.getSpeechStyle()
                : user.getToneType();

        user.updateAiSettings(finalMbti, finalTone);
    }
        // 참고: User 엔티티의 updateAiSettings가 두 인자를 다 받으므로 위와 같이 처리하거나,
        // 엔티티에 개별 setter/update 메서드를 만드는 것이 좋습니다.
}