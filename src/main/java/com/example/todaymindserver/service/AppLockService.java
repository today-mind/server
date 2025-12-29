package com.example.todaymindserver.service;

import com.example.todaymindserver.domain.BusinessException;
import com.example.todaymindserver.domain.user.AppLockErrorCode;
import com.example.todaymindserver.domain.user.User;
import com.example.todaymindserver.domain.app_lock.AppLockErrorCode;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 앱 잠금 설정 및 인증 서비스 (Production Ready)
 */
@Service
@RequiredArgsConstructor
public class AppLockService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 앱 잠금 설정 및 해제
     */
    @Transactional
    public void setAppLock(Long userId, String password) {
        User user = userService.getUser(userId);

        // password가 빈 문자열("") 혹은 공백으로 오면 잠금 해제(null 처리)로 판단합니다.
        if (!StringUtils.hasText(password)) {
            user.updatePassword(null);
            return;
        }

        // 유효한 4자리 숫자가 들어온 경우에만 암호화하여 저장합니다.
        user.updatePassword(passwordEncoder.encode(password));
    }

    @Transactional(readOnly = true)
    public void verifyAppLock(Long userId, String inputPassword) {
        User user = userService.getUser(userId);

        if (user.getPassword() == null) {
            throw new BusinessException(AppLockErrorCode.APP_LOCK_NOT_SET);
        }

        if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
            throw new BusinessException(AppLockErrorCode.INVALID_APP_PASSWORD);
        }
    }
}