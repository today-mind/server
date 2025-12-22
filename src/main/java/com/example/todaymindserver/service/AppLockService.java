package com.example.todaymindserver.service;

import com.example.todaymindserver.common.exception.BusinessException;
import com.example.todaymindserver.common.exception.ErrorCode;
import com.example.todaymindserver.entity.User;
import com.example.todaymindserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 앱 잠금 관련 비즈니스 로직을 담당하는 서비스입니다.
 */
@Service
@RequiredArgsConstructor
public class AppLockService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 앱 잠금 비밀번호 설정
     * @param userId 사용자 식별자
     * @param password 설정할 평문 비밀번호
     */
    @Transactional
    public void setAppLock(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 암호화하여 저장
        user.updatePassword(passwordEncoder.encode(password));
    }

    /**
     * 앱 잠금 비밀번호 확인 (인증)
     * @param userId 사용자 식별자
     * @param inputPassword 입력받은 평문 비밀번호
     */
    @Transactional(readOnly = true)
    public void verifyAppLock(Long userId, String inputPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 잠금 비밀번호 설정 여부 확인
        if (user.getPassword() == null) {
            throw new BusinessException(ErrorCode.APP_LOCK_NOT_SET);
        }

        // 암호화된 비밀번호와 매칭 확인
        if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_APP_PASSWORD);
        }
    }
}

/**
 * 전역 예외 처리를 담당하는 Advice (참고용)
 */
/*
@RestControllerAdvice
class GlobalExceptionHandler {

    // 1. 서비스에서 던지는 BusinessException을 구체적으로 처리해야 500 에러가 안 납니다.
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage()));
    }

    // 2. 그 외 예상치 못한 에러만 500으로 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        return ResponseEntity
                .status(500)
                .body(ApiResponse.error(500, "COMMON_ERROR", "서버 내부 오류가 발생했습니다."));
    }
}
*/