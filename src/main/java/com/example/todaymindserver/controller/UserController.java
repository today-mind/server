package com.example.todaymindserver.controller;

import com.example.todaymindserver.common.response.ApiResponse;
import com.example.todaymindserver.common.response.dto.ProfileResponseDto;
import com.example.todaymindserver.dto.request.AiSettingsRequestDto;
import com.example.todaymindserver.dto.request.AppLockRequestDto;
import com.example.todaymindserver.dto.request.NicknameRequestDto;
import com.example.todaymindserver.dto.response.NicknameResponseDto;
import com.example.todaymindserver.service.AppLockService;
import com.example.todaymindserver.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AppLockService appLockService;

    /**
     * 1. 닉네임 설정 (PATCH /api/users/me/nickname)
     */
    @PatchMapping("/me/nickname")
    public ApiResponse<NicknameResponseDto> setupNickname(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid NicknameRequestDto request) {

        log.info("닉네임 설정 요청 - UserID: {}, NewNickname: {}", userId, request.getNickname());
        NicknameResponseDto updatedNickname = userService.setupNickname(userId, request);
        return ApiResponse.success("닉네임 설정 완료", updatedNickname);
    }

    /**
     * 2. 마이페이지 프로필 정보 조회 (GET /api/users/me/profile)
     */
    @GetMapping("/me/profile")
    public ApiResponse<ProfileResponseDto> getProfile(
            @AuthenticationPrincipal Long userId) { // ✅ 인증된 유저 정보 조회

        log.info("프로필 조회 요청 - UserID: {}", userId);
        ProfileResponseDto profile = userService.getProfile(userId);
        return ApiResponse.success("프로필 정보 조회 완료", profile);
    }

    /**
     * 3. 앱 잠금 비밀번호 설정 (PATCH /api/users/lock-setting/set)
     */
    @PatchMapping("/lock-setting/set")
    public ResponseEntity<ApiResponse<Void>> setAppLock(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AppLockRequestDto request
    ) {

        appLockService.setAppLock(userId, request.getPassword());

        String successMessage = StringUtils.hasText(request.getPassword())
                ? "앱 잠금 설정이 완료되었습니다."
                : "앱 잠금이 해제되었습니다.";

        return ResponseEntity.ok(ApiResponse.success(successMessage, null));
    }

    /**
     * 4. 앱 잠금 비밀번호 확인 (POST /api/users/lock-setting/verify)
     */
    @PostMapping("/lock-setting/verify")
    public ResponseEntity<ApiResponse<Void>> verifyAppLock(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AppLockRequestDto request) {

        appLockService.verifyAppLock(userId, request.getPassword());

        return ResponseEntity.ok(ApiResponse.success("비밀번호 인증에 성공했습니다.", null));
    }

    /**
     * [Branch 5] AI 답장 설정 변경
     * 명세서 주소: PATCH /api/users/ai-setting
     * 명세서 응답 메시지: "설정이 완료되었습니다."
     */
    @PatchMapping("/ai-setting")
    public ApiResponse<Void> updateAiSettings(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AiSettingsRequestDto request) {

        userService.updateAiSettings(userId, request);
        return ApiResponse.success("설정이 완료되었습니다.",null);
    }

    /**
     * 로그 아웃
     */
    @PostMapping("/me/logout")
    public ApiResponse<Void> logout(
            @AuthenticationPrincipal Long userId
    ) {
        userService.logout(userId);
        return ApiResponse.success("사용자가 로그아웃 완료", null);
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/me")
    public ApiResponse<Void> delete (
            @AuthenticationPrincipal Long userId
    ) {
        userService.delete(userId);
        return ApiResponse.success("사용자 회원탈퇴 완료", null);
    }
}
