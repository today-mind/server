package com.example.todaymindserver.domain.diary_write_status;

import java.time.LocalDate;

import com.example.todaymindserver.domain.BaseTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiaryWriteStatus extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_write_status_id")
    private Long id;

    /**
     * 사용자 고유 식별자
     */
    @Column(name = "provider_user_id", nullable = false)
    private String userId;

    /*
     * 일기 식별값
     */
    @Column(name = "diary_id")
    private Long diaryId;

    /**
     * 일기 기준 날짜 (하루 1회 정책의 기준)
     */
    @Column(name = "write_date", nullable = false)
    private LocalDate writeDate;

    /**
     * 일기 작성 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private DiaryWriteStatusType status;

    public static DiaryWriteStatus create(String userId, LocalDate writeDate) {
        DiaryWriteStatus entity = new DiaryWriteStatus();
        entity.userId = userId;
        entity.writeDate = writeDate;
        entity.status = DiaryWriteStatusType.BEFORE;
        return entity;
    }

    public boolean isBefore() {
        return status == DiaryWriteStatusType.BEFORE;
    }

    public void markWritten() {
        this.status = DiaryWriteStatusType.WRITTEN;
    }

    public void markDeleted() {
        this.status = DiaryWriteStatusType.DELETED;
    }

    public void linkDiary(Long diaryId) {
        this.diaryId = diaryId;
    }

    public void unLinkDiary() {
        this.diaryId = null;
    }
}