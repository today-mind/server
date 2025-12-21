package com.example.todaymindserver.service;

import com.example.todaymindserver.common.exception.BusinessException;
import com.example.todaymindserver.common.exception.ErrorCode;
import com.example.todaymindserver.common.response.dto.ProfileResponseDto;
import com.example.todaymindserver.common.util.ToneType;
import com.example.todaymindserver.dto.request.NicknameRequestDto;
import com.example.todaymindserver.dto.response.NicknameResponseDto;
import com.example.todaymindserver.entity.User;
import com.example.todaymindserver.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
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
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. (ID: " + userId + ")"));

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
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 엔티티(User)를 응답 DTO로 변환하여 반환
        return ProfileResponseDto.builder()
                .nickname(user.getNickName())
                .mbtiType(user.getMbtiType())
                .toneType(user.getToneType())
                .build();
    }

    /**
     * [기능 5] AI 답장 톤앤매너 설정 업데이트
     * <p>why: 사용자가 선택한 말투(String)를 Enum으로 변환하여 안전하게 DB에 반영합니다.</p>
     * * @param userId 인증된 사용자 ID
     * @param toneTypeStr 요청받은 말투 문자열 (예: "FRIENDLY", "CASUAL")
     */
    @Transactional
    public void updateAiTone(Long userId, String toneTypeStr) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // DTO에서 넘어온 String이 Enum에 없는 값이라면,
        // 컨트롤러 진입 전 HttpMessageNotReadableException에서 이미 걸러집니다.
        ToneType toneType = ToneType.valueOf(toneTypeStr);
        user.updateAiSettings(user.getMbtiType(), toneType);
    }
}