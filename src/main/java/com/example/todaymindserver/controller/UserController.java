package com.example.todaymindserver.controller;

import com.example.todaymindserver.common.response.ApiResponse;
import com.example.todaymindserver.dto.request.NicknameRequestDto;
import com.example.todaymindserver.dto.response.NicknameResponseDto;
import com.example.todaymindserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.todaymindserver.common.response.dto.ProfileResponseDto;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
}