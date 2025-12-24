package com.example.todaymindserver.entity;

import com.example.todaymindserver.common.util.MbtiType;
import com.example.todaymindserver.common.util.OauthProviderType;
import com.example.todaymindserver.common.util.ToneType;
import com.example.todaymindserver.common.util.UserStatus;

import io.micrometer.common.KeyValues;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.Builder;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 사용자 엔티티 클래스
 * [수정 사항]
 * 1. MbtiType.NONE 참조 제거 (빌드 에러 해결 및 정책 준수)
 * 2. MBTI 미설정 시 null로 관리하도록 기본값 제거
 * 3. UserDetails 구현 유지 및 필드 정리
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OauthProviderType provider;

    @Column(nullable = false, unique = true)
    private String providerUserId;

    private String password; // 앱 잠금용 암호화된 비밀번호

    /**
     * [리뷰 반영] 정책에 따라 기본값 T 설정 및 null 허용 안 함
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private MbtiType mbtiType = MbtiType.T;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ToneType toneType = ToneType.HONORIFIC;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // OAuth용 정적 팩토리 메서드
    public static User create(String email, OauthProviderType provider, String providerUserId, String nickName) {
        return User.builder()
                .email(email)
                .provider(provider)
                .providerUserId(providerUserId)
                .nickName(nickName)
                .mbtiType(MbtiType.T)
                .status(UserStatus.ACTIVE)
                .toneType(ToneType.HONORIFIC)
                .build();
    }

    // 닉네임 수정
    public void updateNickname(String nickname) {
        this.nickName = nickname;
    }

    // 앱 잠금 비밀번호 수정
    public void updatePassword(String password) {
        this.password = password;
    }

    // AI 설정 수정 (ToneType은 null이 들어오지 않도록 방어 로직 권장)
    public void updateAiSettings(MbtiType mbtiType, ToneType toneType) {
        this.mbtiType = mbtiType;
        if (toneType != null) {
            this.toneType = toneType;
        }
    }

    // --- UserDetails 오버라이드 메서드 ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return String.valueOf(this.userId); // 인증 주체로 userId 사용
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == UserStatus.ACTIVE;
    }
}