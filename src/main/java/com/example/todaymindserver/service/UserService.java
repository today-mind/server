package com.example.todaymindserver.service;

import com.example.todaymindserver.common.response.dto.ProfileResponseDto;
import com.example.todaymindserver.dto.request.NicknameRequestDto;
import com.example.todaymindserver.dto.response.NicknameResponseDto;
import com.example.todaymindserver.entity.User;
import com.example.todaymindserver.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    public void updateNickname(Long userId, @NotBlank(message = "닉네임은 필수 항목입니다.") @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 설정해야 합니다.") @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$", message = "닉네임은 특수문자를 포함할 수 없습니다.") String nickname) {
    }

    /* 3. AI 설정 변경 */
    // 이 외에 updatePassword, updateAiSettings 등 나머지 MyPage 로직이 여기에 추가될 수 있습니다.
}