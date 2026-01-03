package com.example.todaymindserver.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todaymindserver.domain.BusinessException;
import com.example.todaymindserver.domain.diary.DiaryErrorCode;
import com.example.todaymindserver.domain.diary_write_status.DiaryWriteStatus;
import com.example.todaymindserver.domain.user.User;
import com.example.todaymindserver.repository.DiaryWriteStatusRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryWriteStatusService {

    private final DiaryWriteStatusRepository diaryWriteStatusRepository;

    @Transactional
    public DiaryWriteStatus validateDiaryWritable(User user) {

        LocalDate today = LocalDate.now();

        DiaryWriteStatus status = diaryWriteStatusRepository.findByUserIdAndWriteDate(user.getProviderUserId(), today)
            .orElseGet(() -> diaryWriteStatusRepository.save(
                DiaryWriteStatus.create(user.getProviderUserId(), today)
            ));

        if (!status.isBefore()) {
            log.warn("일기는 하루에 한 번만 작성할 수 있습니다. userId={}", user.getUserId());
            throw new BusinessException(DiaryErrorCode.DIARY_ALREADY_EXISTS_TODAY);
        }

        return status;
    }

    @Transactional
    public DiaryWriteStatus getDiaryWriteStatus(User user, LocalDate writeDate) {
        return diaryWriteStatusRepository.findByUserIdAndWriteDate(user.getProviderUserId(), writeDate)
            .orElseGet(() -> diaryWriteStatusRepository.save(
                DiaryWriteStatus.create(user.getProviderUserId(), writeDate)
            ));
    }

}
