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
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 관련 모든 설정을 담당하는 컨트롤러
 * 닉네임, 프로필, 앱 잠금 기능을 포함합니다.
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AppLockService appLockService;

    /**
     * 인증 정보(Principal)에서 안전하게 UserId를 추출하는 헬퍼 메서드
     */
    private Long getUserIdSafely(Object principal) {
        if (principal instanceof Long) {
            return (Long) principal;
        }
        if (principal instanceof String) {
            try {
                return Long.parseLong((String) principal);
            } catch (NumberFormatException e) {
                log.error("Principal String을 Long으로 변환할 수 없습니다: {}", principal);
            }
        }

        // 인증 정보가 없거나 타입이 맞지 않을 경우
        // Swagger 테스트를 위해 로그를 남기고 기본값 1L 반환
        log.warn("인증된 사용자 정보가 없습니다. 기본 ID 1L로 진행합니다. (현재 타입: {})",
                principal != null ? principal.getClass().getSimpleName() : "null");
        return 1L;
    }

    /**
     * [기능 1] 닉네임 설정
     */
    @PatchMapping("/me/nickname")
    public ResponseEntity<ApiResponse<NicknameResponseDto>> setupNickname(
            @AuthenticationPrincipal Object principal,
            @RequestBody @Valid NicknameRequestDto request) {

        Long targetId = getUserIdSafely(principal);
        log.info("닉네임 변경 요청 - 유저 ID: {}, 변경할 닉네임: {}", targetId, request.getNickname());

        NicknameResponseDto response = userService.setupNickname(targetId, request);
        return ResponseEntity.ok(ApiResponse.success("닉네임 설정 완료", response));
    }

    /**
     * [기능 2] 프로필 정보 조회
     */
    @GetMapping("/me/profile")
    public ResponseEntity<ApiResponse<ProfileResponseDto>> getProfile(
            @AuthenticationPrincipal Object principal) {

        Long targetId = getUserIdSafely(principal);
        ProfileResponseDto profile = userService.getProfile(targetId);
        return ResponseEntity.ok(ApiResponse.success("프로필 정보 조회 완료", profile));
    }

    /**
     * [기능 4] 앱 잠금 비밀번호 설정
     */
    @PostMapping("/lock-setting/set")
    public ResponseEntity<ApiResponse<Void>> setAppLock(
            @AuthenticationPrincipal Object principal,
            @RequestBody @Valid AppLockRequestDto request) {

        Long targetId = getUserIdSafely(principal);
        appLockService.setAppLock(targetId, request.getPassword());
        return ResponseEntity.ok(ApiResponse.success("앱 잠금 설정이 완료되었습니다.", null));
    }

    /**
     * [기능 4] 앱 잠금 비밀번호 확인 (인증)
     */
    @PostMapping("/lock-setting/verify")
    public ResponseEntity<ApiResponse<Void>> verifyAppLock(
            @AuthenticationPrincipal Object principal,
            @RequestBody @Valid AppLockRequestDto request) {

        Long targetId = getUserIdSafely(principal);
        log.info("잠금 비밀번호 확인 요청 - 유저 ID: {}", targetId);

        appLockService.verifyAppLock(targetId, request.getPassword());
        return ResponseEntity.ok(ApiResponse.success("비밀번호 인증 성공", null));
    }

    /**
     * [기능 5] AI 답장 톤 설정
     */
    @PatchMapping("/ai-setting")
    public ResponseEntity<ApiResponse<Void>> updateAiTone(
            @AuthenticationPrincipal Object principal,
            @RequestBody @Valid AiToneRequestDto request) {

        Long targetId = getUserIdSafely(principal);

        userService.updateAiTone(targetId, request.getToneType());

        return ResponseEntity.ok(ApiResponse.success("설정이 완료되었습니다.", null));
    }
}