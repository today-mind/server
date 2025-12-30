package com.example.todaymindserver.repository;

import com.example.todaymindserver.domain.diary.Diary;
import com.example.todaymindserver.domain.user.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Diary 엔티티 데이터 접근 계층 (Repository)
 */
@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<Diary> findByUserAndCreatedAtBetweenAndDeletedAtIsNullOrderByCreatedAtDesc(User user, LocalDateTime start, LocalDateTime end);

    boolean existsByUserAndCreatedAtBetween(User user, LocalDateTime createdAtAfter, LocalDateTime createdAtBefore);

    List<Diary> findAllByUser_UserIdAndDeletedAtIsNull(Long userUserId);

    Optional<Diary> findByDiaryIdAndDeletedAtIsNull(Long diaryId);
}