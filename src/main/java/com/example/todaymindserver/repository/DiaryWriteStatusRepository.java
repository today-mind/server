package com.example.todaymindserver.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todaymindserver.domain.diary_write_status.DiaryWriteStatus;

public interface DiaryWriteStatusRepository extends JpaRepository<DiaryWriteStatus, Long> {
    Optional<DiaryWriteStatus> findByUserIdAndWriteDate(String userId, LocalDate writeDate);
}

