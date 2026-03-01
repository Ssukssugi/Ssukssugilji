package com.ssukssugi.ssukssugilji.plant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DiaryApplication {

    private final DiaryService diaryService;
    private final GrowthService growthService;

    @Transactional
    public void deleteDiary(Long diaryId) {
        growthService.cleanGrowthsByDiaryId(diaryId);
        diaryService.deleteDiary(diaryId);
    }
}
