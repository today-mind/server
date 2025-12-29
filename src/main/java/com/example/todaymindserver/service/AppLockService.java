package com.example.todaymindserver.service;

import com.example.todaymindserver.domain.BusinessException;
import com.example.todaymindserver.domain.user.User;
import com.example.todaymindserver.domain.app_lock.AppLockErrorCode;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * [Branch 4 최종] 앱 잠금 서비스
 */
@Service
@RequiredArgsConstructor
public class AppLockService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void setAppLock(Long userId, String password) {
        User user = userService.getUser(userId);

        // 비밀번호 암호화 저장
        user.updatePassword(passwordEncoder.encode(password));
    }

    @Transactional(readOnly = true)
    public void verifyAppLock(Long userId, String inputPassword) {
        User user = userService.getUser(userId);

        // 2. 앱 잠금 전용 에러(설정 안 됨) 사용
        if (user.getPassword() == null) {
            throw new BusinessException(AppLockErrorCode.APP_LOCK_NOT_SET);
        }

        // 3. 앱 잠금 전용 에러(비밀번호 불일치) 사용
        if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
            throw new BusinessException(AppLockErrorCode.INVALID_APP_PASSWORD);
        }
    }
}