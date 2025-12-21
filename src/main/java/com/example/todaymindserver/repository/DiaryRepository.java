package com.example.todaymindserver.repository;

import com.example.todaymindserver.entity.Diary;
import com.example.todaymindserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    /**
     * 특정 유저의 특정 기간(한 달) 동안의 일기를 생성일 역순으로 조회합니다.
     * why: 5번 통계 기능과 3번 캘린더 기능을 위해 필수적인 메서드입니다.
     */
    List<Diary> findByUserUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(Long userId, LocalDateTime start, LocalDateTime end);

    List<Diary> findByUserAndCreatedAtBetweenOrderByCreatedAtDesc(User user, LocalDateTime start, LocalDateTime end);
}