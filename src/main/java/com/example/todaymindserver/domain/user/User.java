package com.example.todaymindserver.domain.user;

import com.example.todaymindserver.domain.oauth.OauthProviderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String nickName;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private OauthProviderType provider;

    @Column(nullable = false, unique = true)
    private String providerUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MbtiType mbtiType = MbtiType.T;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ToneType toneType = ToneType.HONORIFIC;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private String password;

    private User(String email, OauthProviderType provider, String providerUserId) {
        this.email = email;
        this.provider = provider;
        this.providerUserId = providerUserId;
    }

    public static User create(String email, OauthProviderType provider, String providerUserId) {
        return new User(email, provider, providerUserId);
    }

    public void updateNickname(String nickname) {
        this.nickName = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateMbtiType(MbtiType mbtiType) {
        if (mbtiType != null) {
            this.mbtiType = mbtiType;
        }
    }

    public void updateToneType(ToneType toneType) {
        if (toneType != null) {
            this.toneType = toneType;
        }
    }
}