package com.ssukssugi.ssukssugilji.plant.service;

import com.ssukssugi.ssukssugilji.plant.controller.dto.GrowthIntroduceRequest;
import com.ssukssugi.ssukssugilji.plant.entity.Diary;
import com.ssukssugi.ssukssugilji.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GrowthApplication {

    private final GrowthService growthService;
    private final DiaryService diaryService;

    @Transactional
    public void createGrowth(User user, GrowthIntroduceRequest request) {
        Diary before = diaryService.getById(request.beforeDiaryId());
        Diary after = diaryService.getById(request.afterDiaryId());
        if (!before.getPlant().equals(after.getPlant())) {
            throw new IllegalArgumentException("The diaries do not belong to the same plant.");
        }

        growthService.createEntity(user, before, after);
    }
}
