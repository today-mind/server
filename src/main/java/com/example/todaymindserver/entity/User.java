package com.example.todaymindserver.entity;

import com.example.todaymindserver.common.util.MbtiType;
import com.example.todaymindserver.common.util.OauthProviderType;
import com.example.todaymindserver.common.util.ToneType;
import com.example.todaymindserver.common.util.UserStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String nickName;

    private String email;

    @Enumerated(EnumType.STRING)
    private OauthProviderType provider;

    @Column(nullable = false, unique = true)
    private String providerUserId;

    // 2. 프로필/설정 정보 (당신 담당: 닉네임)
    @Column
    private String nickname;

    // AI 페르소나 설정 (MyPage API 담당)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MbtiType mbtiType = MbtiType.T;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ToneType toneType = ToneType.HONORIFIC;

    // 3. 회원 상태 (MyPage API 담당)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    // 4. Auditing
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

    public static User create (String email, OauthProviderType provider, String providerUserId) {
        return new User(
            email,
            provider,
            providerUserId
        );
    }

    // 닉네임 설정을 위한 메서드
    public void updateNickname(String nickname) {
        this.nickName = nickname;
    }

    // 앱 잠금 비밀번호 설정을 위한 메서드
    public void updatePassword(String password) {
        this.password = password;
    }

    // AI 설정 변경을 위한 메서드
    public void updateAiSettings(MbtiType mbtiType, ToneType toneType) {
        this.mbtiType = mbtiType;
        this.toneType = toneType;
    }
    // final change //
}