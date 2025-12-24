package com.example.todaymindserver.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 엔티티가 공통으로 가지는 생성/수정 시간 필드를 관리하는 Base Entity
 * <p>why: 여러 엔티티에서 중복되는 시간 관련 필드를 제거하고 상속받아 코드를 깔끔하게 유지합니다.</p>
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTime {

    /**
     * 생성 시각 (Auditing)
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false) // DB 컬럼명 snake_case
    private LocalDateTime createdAt;

    /**
     * 최종 수정 시각 (Auditing)
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false) // DB 컬럼명 snake_case
    private LocalDateTime updatedAt;
}