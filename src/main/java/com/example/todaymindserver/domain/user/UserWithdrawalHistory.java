package com.example.todaymindserver.domain.user;

import java.time.LocalDateTime;

import com.example.todaymindserver.domain.oauth.OauthProviderType;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserWithdrawalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "withdrawal_history_id")
    private Long id;

    /**
     * OAuth Provider (KAKAO, GOOGLE, APPLE ...)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OauthProviderType provider;

    /**
     * OAuth Provider에서 내려주는 유저 고유 ID
     * 재가입 판단의 기준 키
     */
    @Column(name = "provider_user_id", nullable = false)
    private String providerUserId;

    /**
     * 탈퇴 시점
     * 재가입 쿨타임 계산 기준
     */
    @Column(name = "withdrawn_at", nullable = false)
    private LocalDateTime withdrawnAt;

    public static UserWithdrawalHistory create(
        OauthProviderType provider,
        String providerUserId,
        LocalDateTime withdrawnAt
    ) {
        UserWithdrawalHistory history = new UserWithdrawalHistory();
        history.provider = provider;
        history.providerUserId = providerUserId;
        history.withdrawnAt = withdrawnAt;
        return history;
    }

    /**
     * 재가입 제한 여부 판단
     * @param cooldownHours 제한 시간 
     */
    public boolean isCooldownActive(int cooldownHours) {
        return withdrawnAt
            .plusHours(cooldownHours)
            .isAfter(LocalDateTime.now());
    }
}

