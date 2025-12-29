package com.example.todaymindserver.domain.user;

import com.example.todaymindserver.domain.BaseTime;
import com.example.todaymindserver.domain.BusinessException;
import com.example.todaymindserver.domain.oauth.OauthProviderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

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

    public void validateUserNickNameExists() {
        if (getNickName() == null) {
            log.error("사용자의 닉네임이 존재하지 않습니다. userId={}", getUserId());
            throw new BusinessException(UserErrorCode.NICKNAME_REQUIRED);
        }
    }
}