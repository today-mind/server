package com.example.todaymindserver.entity;

import com.example.todaymindserver.common.util.MbtiType;
import com.example.todaymindserver.common.util.OauthProviderType;
import com.example.todaymindserver.common.util.ToneType;
import com.example.todaymindserver.common.util.UserStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

// --- 추가된 Import ---
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; // UserDetails 추가
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String nickName; // Unique 제거된 상태

    private String email;

    @Enumerated(EnumType.STRING)
    private OauthProviderType provider;

    @Column(nullable = false, unique = true)
    private String providerUserId;

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

    // OAuth용 생성자 (김용준 님 코드)
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

    // --- UserDetails 오버라이드 메서드 (AuthenticationPrincipal 사용 필수) ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 설정이 없으므로 임시로 빈 리스트 반환
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return String.valueOf(this.userId); // 인증 주체(Principal)로 userId를 사용
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