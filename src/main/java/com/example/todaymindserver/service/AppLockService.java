package com.example.todaymindserver.service;

import com.example.todaymindserver.common.exception.BusinessException;
import com.example.todaymindserver.common.exception.ErrorCode;
import com.example.todaymindserver.entity.User;
import com.example.todaymindserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppLockService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void setAppLock(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // User 엔티티에 있는 updatePassword 메서드 활용
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