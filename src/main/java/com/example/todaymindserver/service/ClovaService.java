package com.example.todaymindserver.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todaymindserver.common.client.dto.ClovaResponse;
import com.example.todaymindserver.domain.BusinessException;
import com.example.todaymindserver.domain.diary.Diary;
import com.example.todaymindserver.domain.diary.DiaryErrorCode;
import com.example.todaymindserver.repository.DiaryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClovaService {

    private final DiaryRepository diaryRepository;

    @Transactional
    public void saveClovaResponse(Long diaryId, ClovaResponse response) {
        Diary diary = diaryRepository.findById(diaryId).orElse(null);

        if (diary == null) {
            log.error("해당 ID는 일기가 존재하지 않습니다. 일기 ID={}", diaryId);
            throw new BusinessException(DiaryErrorCode.DIARY_NOT_FOUND);
        }

        try {
            diary.updateEmpatheticResponse(response.result().message().content());
            diary.updateResponseStatusToCompleted();
        } catch (Exception e) {
            log.error("Clova 공감 답장 저장을 실패하였습니다. 상세 오류: {}", String.valueOf(e));
            diary.updateResponseStatusToFailed();
        }
    }
}
