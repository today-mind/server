package com.example.todaymindserver.controller;

import com.example.todaymindserver.common.response.ApiResponse;
import com.example.todaymindserver.dto.request.NicknameRequestDto;
import com.example.todaymindserver.dto.request.AppLockRequestDto;
import com.example.todaymindserver.dto.response.NicknameResponseDto;
import com.example.todaymindserver.common.response.dto.ProfileResponseDto;
import com.example.todaymindserver.service.UserService;
import com.example.todaymindserver.service.AppLockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AppLockService appLockService;

    /** 1. 닉네임 설정 */
    @PatchMapping("/me/nickname")
    public ApiResponse<NicknameResponseDto> setupNickname(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid NicknameRequestDto request) {

        NicknameResponseDto response = userService.setupNickname(userId, request);
        return ApiResponse.success("닉네임 설정 완료", response);
    }

    /** 2. 프로필 조회 */
    @GetMapping("/me/profile")
    public ApiResponse<ProfileResponseDto> getProfile(@AuthenticationPrincipal Long userId) {
        ProfileResponseDto profile = userService.getProfile(userId);
        return ApiResponse.success("프로필 정보 조회 완료", profile);
    }

    /** 4. 앱 잠금 설정 */
    @PostMapping("/lock-setting/set")
    public ApiResponse<Void> setAppLock(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AppLockRequestDto request) {

        appLockService.setAppLock(userId, request.getPassword());
        return ApiResponse.success("앱 잠금 설정이 완료되었습니다.", null);
    }

    /** 4. 앱 잠금 확인 */
    @PostMapping("/lock-setting/verify")
    public ApiResponse<Void> verifyAppLock(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AppLockRequestDto request) {

        appLockService.verifyAppLock(userId, request.getPassword());
        return ApiResponse.success("비밀번호 인증 성공", null);
    }
}