package com.example.todaymindserver.controller;

import com.example.todaymindserver.common.response.ApiResponse;
import com.example.todaymindserver.common.response.dto.ProfileResponseDto;
import com.example.todaymindserver.dto.Message;
import com.example.todaymindserver.dto.request.AppLockRequestDto;
import com.example.todaymindserver.dto.request.NicknameRequestDto;
import com.example.todaymindserver.dto.response.NicknameResponseDto;
import com.example.todaymindserver.service.AppLockService;
import com.example.todaymindserver.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AppLockService appLockService;
    private Logger log;

    // 1. 닉네임 설정 (PATCH /api/users/me/nickname)
    @PatchMapping("/users/me/nickname")
    public ApiResponse<NicknameResponseDto> setupNickname(
            // @AuthenticationPrincipal Long userId, <----- 이 줄은 임시로 주석 처리하거나 삭제합니다.
            @RequestBody NicknameRequestDto request) {

        // [임시 코드] Security 구현 전까지는 임시 User ID를 사용합니다.
        Long userId = 1L; // 1번 사용자로 임시 지정

        NicknameResponseDto updatedNickname = userService.setupNickname(userId, request);
        return ApiResponse.success("닉네임 설정 완료", updatedNickname);
    }

    // 2. 마이페이지 프로필 정보 조회
    @GetMapping("/users/me/profile")
    public ApiResponse<ProfileResponseDto> getProfile(
            // @AuthenticationPrincipal Long userId <----- 이 줄도 임시로 주석 처리하거나 삭제합니다.
    ) {

        // [임시 코드] Security 구현 전까지는 임시 User ID를 사용합니다.
        Long userId = 1L; // 1번 사용자로 임시 지정

        ProfileResponseDto profile = userService.getProfile(userId);
        return ApiResponse.success("프로필 정보 조회 완료", profile);
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

        log.info("잠금 비밀번호 확인 요청 - 유저 ID: {}");
        appLockService.verifyAppLock(userId, request.getPassword());

        return ResponseEntity.ok(ApiResponse.success("비밀번호 인증에 성공했습니다.", null));
    }
}