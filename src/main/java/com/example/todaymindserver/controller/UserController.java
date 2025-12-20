package com.example.todaymindserver.controller;

import com.example.todaymindserver.common.response.ApiResponse;
import com.example.todaymindserver.dto.request.NicknameRequestDto;
import com.example.todaymindserver.dto.request.AppLockRequestDto; // 추가될 DTO
import com.example.todaymindserver.dto.response.NicknameResponseDto;
import com.example.todaymindserver.common.response.dto.ProfileResponseDto;
import com.example.todaymindserver.service.UserService;
import com.example.todaymindserver.service.AppLockService; // 추가될 서비스
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AppLockService appLockService; // 4번 기능을 담당할 서비스

    // 1. 닉네임 설정
    @PatchMapping("/me/nickname")
    public ApiResponse<NicknameResponseDto> setupNickname(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid NicknameRequestDto request) {

        Long targetId = (userId == null) ? 1L : userId;
        NicknameResponseDto updatedNickname = userService.setupNickname(targetId, request);
        return ApiResponse.success("닉네임 설정 완료", updatedNickname);
    }

    // 2. 프로필 조회
    @GetMapping("/me/profile")
    public ApiResponse<ProfileResponseDto> getProfile(@AuthenticationPrincipal Long userId) {
        Long targetId = (userId == null) ? 1L : userId;
        ProfileResponseDto profile = userService.getProfile(targetId);
        return ApiResponse.success("프로필 정보 조회 완료", profile);
    }

    // 4. [추가] 앱 잠금 설정/변경
    @PostMapping("/lock-setting/set")
    public ApiResponse<Void> setAppLock(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AppLockRequestDto request) {

        Long targetId = (userId == null) ? 1L : userId;
        appLockService.setAppLock(targetId, request.getPassword());
        return ApiResponse.success("앱 잠금 비밀번호가 설정되었습니다.", null);
    }

    // 4. [추가] 앱 잠금 확인
    @PostMapping("/lock-setting/verify")
    public ApiResponse<Void> verifyAppLock(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AppLockRequestDto request) {

        Long targetId = (userId == null) ? 1L : userId;
        appLockService.verifyAppLock(targetId, request.getPassword());
        return ApiResponse.success("비밀번호 인증에 성공했습니다.", null);
    }
}