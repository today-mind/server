package com.example.todaymindserver.domain.diary;

import com.example.todaymindserver.domain.BusinessException;
import com.example.todaymindserver.domain.user.EmotionType;
import com.example.todaymindserver.domain.BaseTime;
import com.example.todaymindserver.domain.user.User;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener; // EntityListeners는 유지

/**
 * 일기 기록(Diary) 엔티티
 * <p>사용자가 작성한 일기 내용과 감정 유형 등 일기 자체의 정보를 담는다.</p>
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "diaries")
public class Diary extends BaseTime { // <--- BaseTimeEntity 상속 추가

    /**
     * 일기 고유 ID (Primary Key)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long diaryId;

    /**
     * 사용자 외래 키 (FK)
     * <p>why: 한 명의 사용자(User)가 여러 개의 일기(Diary)를 작성하는 1:N 관계를 표현합니다.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 일기 본문 내용
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /**
     * 일기 작성 시 선택한 감정 유형
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "emotion_type", nullable = false, length = 50)
    private EmotionType emotionType;

    @Column(columnDefinition = "TEXT")
    private String empatheticResponse;

    @Enumerated(EnumType.STRING)
    @Column(name = "response_status_type", nullable = false)
    private ResponseStatusType responseStatus = ResponseStatusType.PENDING;

    private Diary(User user, String content, EmotionType emotionType) {
        this.user = user;
        this.content = content;
        this.emotionType = emotionType;
    }

    public static Diary create(User user, String content, EmotionType emotionType) {
        return new Diary(user, content, emotionType);
    }

    public void validateOwner(Long userId) {
        if (!this.user.getUserId().equals(userId)) {
            throw new BusinessException(DiaryErrorCode.DIARY_ACCESS_DENIED);
        }
    }

    public void validateRetryAllowed() {
        if (this.responseStatus != ResponseStatusType.FAILED) {
            throw new BusinessException(DiaryErrorCode.INVALID_RESPONSE_STATUS_TRANSITION);
        }
    }

    public void markResponseCompleted(String response) {
        if (this.responseStatus == ResponseStatusType.COMPLETED) {
            throw new BusinessException(DiaryErrorCode.INVALID_RESPONSE_STATUS_TRANSITION);
        }
        this.empatheticResponse = response;
        this.responseStatus = ResponseStatusType.COMPLETED;
    }

    public void markResponseFailed() {
        if (this.responseStatus == ResponseStatusType.COMPLETED) {
            throw new BusinessException(DiaryErrorCode.INVALID_RESPONSE_STATUS_TRANSITION);
        }
        this.responseStatus = ResponseStatusType.FAILED;
    }
}