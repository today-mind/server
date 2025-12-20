package com.example.todaymindserver.controller;

import com.example.todaymindserver.common.response.ApiResponse;
import com.example.todaymindserver.dto.request.AppLockRequestDto;
import com.example.todaymindserver.entity.User;
import com.example.todaymindserver.service.AppLockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/lock-setting")
@RequiredArgsConstructor
public class AppLockController {

    private final AppLockService appLockService;

    @PostMapping("/set")
    public ApiResponse<Void> setAppLock(
            @AuthenticationPrincipal User user, // UserDetails 구현체이므로 바로 유저 객체 받기
            @RequestBody @Valid AppLockRequestDto request) {

        // 로그인 정보가 없으면 테스트용으로 1L 사용
        Long userId = (user == null) ? 1L : user.getUserId();
        appLockService.setAppLock(userId, request.getPassword());
        return ApiResponse.success("앱 잠금 비밀번호 설정 완료", null);
    }

    @PostMapping("/verify")
    public ApiResponse<Void> verifyAppLock(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid AppLockRequestDto request) {

        Long userId = (user == null) ? 1L : user.getUserId();
        appLockService.verifyAppLock(userId, request.getPassword());
        return ApiResponse.success("비밀번호 인증 성공", null);
    }
}