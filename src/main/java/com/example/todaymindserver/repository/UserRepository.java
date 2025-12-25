package com.example.todaymindserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todaymindserver.domain.oauth.OauthProviderType;
import com.example.todaymindserver.domain.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProviderAndProviderUserId(OauthProviderType provider, String providerUserId);

    Optional<User> findByNickName(String nickName);
}