package com.example.todaymindserver.controller;

import com.example.todaymindserver.common.response.ApiResponse;
import com.example.todaymindserver.dto.request.AiToneRequestDto;
import com.example.todaymindserver.dto.request.NicknameRequestDto;
import com.example.todaymindserver.dto.request.AppLockRequestDto;
import com.example.todaymindserver.dto.response.NicknameResponseDto;
import com.example.todaymindserver.common.response.dto.ProfileResponseDto;
import com.example.todaymindserver.service.UserService;
import com.example.todaymindserver.service.AppLockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 관련 모든 설정을 담당하는 컨트롤러
 * 닉네임, 프로필, AI 말투, 앱 잠금 기능을 모두 포함합니다.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AppLockService appLockService;

    /**
     * [기능 1] 닉네임 설정
     */
    @PatchMapping("/me/nickname")
    public ResponseEntity<ApiResponse<NicknameResponseDto>> setupNickname(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid NicknameRequestDto request) {

        Long targetId = (userId == null) ? 1L : userId;
        NicknameResponseDto response = userService.setupNickname(targetId, request);
        return ResponseEntity.ok(ApiResponse.success("닉네임 설정 완료", response));
    }

    /**
     * [기능 2] 프로필 정보 조회
     */
    @GetMapping("/me/profile")
    public ResponseEntity<ApiResponse<ProfileResponseDto>> getProfile(@AuthenticationPrincipal Long userId) {

        Long targetId = (userId == null) ? 1L : userId;
        ProfileResponseDto profile = userService.getProfile(targetId);
        return ResponseEntity.ok(ApiResponse.success("프로필 정보 조회 완료", profile));
    }

    /**
     * [기능 5] AI 답장 톤 설정
     */
    @PatchMapping("/ai-setting")
    public ResponseEntity<ApiResponse<Void>> updateAiTone(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AiToneRequestDto request) {

        Long targetId = (userId == null) ? 1L : userId;
        userService.updateAiTone(targetId, request.getToneType());
        return ResponseEntity.ok(ApiResponse.success("설정이 완료되었습니다.", null));
    }

    /**
     * [기능 4] 앱 잠금 비밀번호 설정
     */
    @PostMapping("/lock-setting/set")
    public ResponseEntity<ApiResponse<Void>> setAppLock(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AppLockRequestDto request) {

        Long targetId = (userId == null) ? 1L : userId;
        appLockService.setAppLock(targetId, request.getPassword());
        return ResponseEntity.ok(ApiResponse.success("앱 잠금 설정이 완료되었습니다.", null));
    }

    /**
     * [기능 4] 앱 잠금 비밀번호 확인 (인증)
     */
    @PostMapping("/lock-setting/verify")
    public ResponseEntity<ApiResponse<Void>> verifyAppLock(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AppLockRequestDto request) {

        Long targetId = (userId == null) ? 1L : userId;
        appLockService.verifyAppLock(targetId, request.getPassword());
        return ResponseEntity.ok(ApiResponse.success("비밀번호 인증 성공", null));
    }
}