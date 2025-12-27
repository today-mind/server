package com.example.todaymindserver.repository;

import com.example.todaymindserver.domain.diary.Diary;
import com.example.todaymindserver.domain.user.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Diary 엔티티 데이터 접근 계층 (Repository)
 */
@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    /**
     * 특정 사용자의 특정 기간 (월) 일기를 작성일 기준 내림차순으로 조회합니다.
     * <p>why: 캘린더 뷰에서 해당 월에 작성된 일기 목록을 빠르게 가져오기 위함입니다.</p>
     *
     * @param user 사용자 entity
     * @param startOfMonth 해당 월의 시작일 (예: 2025-10-01 00:00:00)
     * @param endOfMonth 해당 월의 마지막 날 (예: 2025-10-31 23:59:59)
     * @return 해당 기간의 일기 목록 (List<Diary>)
     */
    List<Diary> findByUserAndCreatedAtBetweenOrderByCreatedAtDesc(
        User user,
        LocalDateTime startOfMonth,
        LocalDateTime endOfMonth
    );

    /**
     * 특정 기간 내의 일기 목록 조회
     * JPQL을 사용하여 사용자와 생성일 범위를 조건으로 데이터를 가져옵니다.
     */
    @Query("SELECT d FROM Diary d WHERE d.user = :user AND d.createdAt >= :start AND d.createdAt < :end")
    List<Diary> findAllByUserAndCreatedAtBetween(
            @Param("user") User user,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    void deleteAllByUser_UserId(Long userUserId);

    boolean existsByUserAndCreatedAtBetween(
        User user,
        LocalDateTime start,
        LocalDateTime end
    );
}