package com.example.todaymindserver.entity;

import com.example.todaymindserver.common.util.OauthProviderType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String nickName;

    private String email;

    @Enumerated(EnumType.STRING)
    private OauthProviderType provider;

    @Column(nullable = false, unique = true)
    private String providerUserId;

    private User(String email, OauthProviderType provider, String providerUserId) {
        this.email = email;
        this.provider = provider;
        this.providerUserId = providerUserId;
    }

    public static User create (String email, OauthProviderType provider, String providerUserId) {
        return new User(
            email,
            provider,
            providerUserId
        );
    }
}
