package com.example.todaymindserver.domain.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.todaymindserver.domain.BusinessException;
import com.example.todaymindserver.domain.oauth.OauthProviderType;
import com.example.todaymindserver.repository.UserWithdrawalHistoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRejoinPolicy {

    private final UserWithdrawalHistoryRepository historyRepository;

    @Value("${policy.rejoin-cooldown-hours}")
    private int rejoinCooldownHours;

    public void validateRejoinAllowed(
        OauthProviderType provider,
        String providerUserId
    ) {
        historyRepository.findByProviderAndProviderUserId(provider, providerUserId)
            .ifPresent(history -> {
                if (history.isCooldownActive(rejoinCooldownHours)) {
                    throw new BusinessException(
                        UserErrorCode.REJOIN_COOLDOWN_ACTIVE,
                        String.format(
                            "회원 탈퇴 후 %s시간 이내에 재가입이 불가능합니다.",
                            rejoinCooldownHours
                        )
                    );
                }
            });
    }
}

