package com.example.todaymindserver.controller;

import com.example.todaymindserver.common.exception.BusinessException;
import com.example.todaymindserver.common.exception.ErrorCode;
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

/**
 * 사용자 관련 API 컨트롤러
 * <p>리뷰어 피드백 반영: 임시 로직(1L) 제거 및 에러 코드 체계 복구</p>
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AppLockService appLockService;

    @PatchMapping("/me/nickname")
    public ApiResponse<NicknameResponseDto> setupNickname(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid NicknameRequestDto request) {

        validateUserAuthentication(userId);
        NicknameResponseDto response = userService.setupNickname(userId, request);
        return ApiResponse.success("닉네임 설정이 완료되었습니다.", response);
    }

    @GetMapping("/me/profile")
    public ApiResponse<ProfileResponseDto> getProfile(@AuthenticationPrincipal Long userId) {

        validateUserAuthentication(userId);
        ProfileResponseDto profile = userService.getProfile(userId);
        return ApiResponse.success("프로필 정보 조회가 완료되었습니다.", profile);
    }

    @PostMapping("/lock-setting/set")
    public ApiResponse<Void> setAppLock(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AppLockRequestDto request) {

        validateUserAuthentication(userId);
        appLockService.setAppLock(userId, request.getPassword());
        return ApiResponse.success("앱 잠금 비밀번호 설정이 완료되었습니다.", null);
    }

    @PostMapping("/lock-setting/verify")
    public ApiResponse<Void> verifyAppLock(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AppLockRequestDto request) {

        validateUserAuthentication(userId);
        appLockService.verifyAppLock(userId, request.getPassword());
        return ApiResponse.success("비밀번호 인증에 성공하였습니다.", null);
    }

    /**
     * 인증 여부 검증
     */
    private void validateUserAuthentication(Long userId) {
        if (userId == null) {
            // ErrorCode.USER_NOT_FOUND 혹은 UNAUTHORIZED 등을 사용하세요.
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
    }
}