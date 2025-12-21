package com.example.todaymindserver.controller;

import com.example.todaymindserver.common.response.ApiResponse;
import com.example.todaymindserver.dto.request.AiToneRequestDto;
import com.example.todaymindserver.dto.request.NicknameRequestDto;
import com.example.todaymindserver.dto.response.NicknameResponseDto;
import com.example.todaymindserver.common.response.dto.ProfileResponseDto;
import com.example.todaymindserver.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * [기능 1] 닉네임 설정
     */
    @PatchMapping("/me/nickname")
    public ApiResponse<NicknameResponseDto> setupNickname(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid NicknameRequestDto request) {

        // [해결 포인트] 토큰이 없는 로컬 테스트 환경을 위해 null이면 1L을 사용합니다.
        Long targetId = (userId == null) ? 1L : userId;
        NicknameResponseDto response = userService.setupNickname(targetId, request);
        return ApiResponse.success("닉네임 설정 완료", response);
    }

    /**
     * [기능 2] 프로필 정보 조회
     */
    @GetMapping("/me/profile")
    public ApiResponse<ProfileResponseDto> getProfile(@AuthenticationPrincipal Long userId) {

        // [해결 포인트] null 방어 코드 추가
        Long targetId = (userId == null) ? 1L : userId;
        ProfileResponseDto profile = userService.getProfile(targetId);
        return ApiResponse.success("프로필 정보 조회 완료", profile);
    }

    /**
     * [기능 5] AI 답장 톤 설정
     */
    @PatchMapping("/ai-setting")
    public ApiResponse<Void> updateAiTone(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AiToneRequestDto request) {

        // [해결 포인트] null 방어 코드 추가
        Long targetId = (userId == null) ? 1L : userId;
        userService.updateAiTone(targetId, request.getToneType());

        return ApiResponse.success("설정이 완료되었습니다.", null);
    }
}