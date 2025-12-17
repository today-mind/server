package com.example.todaymindserver.repository;

import com.example.todaymindserver.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Diary 엔티티 데이터 접근 계층 (Repository)
 */
@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
}