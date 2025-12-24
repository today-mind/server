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
 * [빌드 에러 해결] static 키워드를 제거하여 인스턴스 메서드로 변경했습니다.
 */
@Service
@RequiredArgsConstructor
public class AppLockService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void setAppLock(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 암호화하여 저장
        user.updatePassword(passwordEncoder.encode(password));
    }

    @Transactional(readOnly = true)
    public void verifyAppLock(Long userId, String inputPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (user.getPassword() == null) {
            throw new BusinessException(ErrorCode.APP_LOCK_NOT_SET);
        }

        if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_APP_PASSWORD);
        }
    }
}