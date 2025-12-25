package com.example.todaymindserver.domain.token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    private Long userId;

    @Column(nullable = false, length = 500)
    private String token;

    private RefreshToken(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public static RefreshToken create(Long userId, String token) {
        return new RefreshToken(userId, token);
    }

    public void updateToken(String token) {
        this.token = token;
    }
}