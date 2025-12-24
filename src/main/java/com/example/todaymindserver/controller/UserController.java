package com.example.todaymindserver.controller;

import com.example.todaymindserver.common.response.ApiResponse;
import com.example.todaymindserver.common.response.dto.ProfileResponseDto;
import com.example.todaymindserver.dto.request.AppLockRequestDto;
import com.example.todaymindserver.dto.request.NicknameRequestDto;
import com.example.todaymindserver.service.AppLockService;
import com.example.todaymindserver.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * [Branch 4 최종] 사용자 설정 및 앱 잠금 컨트롤러
 * 리뷰어 피드백 반영: Object principal 제거 및 Long userId 직접 사용
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AppLockService appLockService;

    /**
     * [기능 1] 닉네임 설정/변경
     */
    @PatchMapping("/me/nickname")
    public ResponseEntity<ApiResponse<Void>> updateNickname(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid NicknameRequestDto request) {

        log.info("닉네임 변경 요청 - 유저 ID: {}, 변경할 닉네임: {}", userId, request.getNickname());
        userService.updateNickname(userId, request.getNickname());

        return ResponseEntity.ok(ApiResponse.success("닉네임이 성공적으로 변경되었습니다.", null));
    }

    /**
     * [기능 2] 프로필 정보 조회
     */
    @GetMapping("/me/profile")
    public ResponseEntity<ApiResponse<ProfileResponseDto>> getProfile(
            @AuthenticationPrincipal Long userId) {

        ProfileResponseDto profile = userService.getProfile(userId);
        return ResponseEntity.ok(ApiResponse.success("프로필 정보 조회 완료", profile));
    }

    /**
     * [기능 4] 앱 잠금 비밀번호 설정
     */
    @PostMapping("/lock-setting/set")
    public ResponseEntity<ApiResponse<Void>> setAppLock(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AppLockRequestDto request) {

        appLockService.setAppLock(userId, request.getPassword());
        return ResponseEntity.ok(ApiResponse.success("앱 잠금 설정이 완료되었습니다.", null));
    }

    /**
     * [기능 4] 앱 잠금 비밀번호 확인 (인증)
     */
    @PostMapping("/lock-setting/verify")
    public ResponseEntity<ApiResponse<Void>> verifyAppLock(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AppLockRequestDto request) {

        log.info("잠금 비밀번호 확인 요청 - 유저 ID: {}", userId);
        appLockService.verifyAppLock(userId, request.getPassword());

        return ResponseEntity.ok(ApiResponse.success("비밀번호 인증에 성공했습니다.", null));
    }
}