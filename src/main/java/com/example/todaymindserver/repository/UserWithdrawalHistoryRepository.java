package com.example.todaymindserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todaymindserver.domain.oauth.OauthProviderType;
import com.example.todaymindserver.domain.user.UserWithdrawalHistory;

public interface UserWithdrawalHistoryRepository extends JpaRepository<UserWithdrawalHistory, Long> {
    Optional<UserWithdrawalHistory> findByProviderAndProviderUserId(OauthProviderType type, String sub);
}
