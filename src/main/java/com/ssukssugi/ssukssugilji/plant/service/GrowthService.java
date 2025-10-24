package com.ssukssugi.ssukssugilji.plant.service;

import com.ssukssugi.ssukssugilji.plant.dao.GrowthRepository;
import com.ssukssugi.ssukssugilji.plant.dto.GrowthIntroduceRequest;
import com.ssukssugi.ssukssugilji.plant.dto.GrowthVo;
import com.ssukssugi.ssukssugilji.plant.dto.GrowthVoListDto;
import com.ssukssugi.ssukssugilji.plant.entity.Diary;
import com.ssukssugi.ssukssugilji.plant.entity.Growth;
import com.ssukssugi.ssukssugilji.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GrowthService {

    private final GrowthRepository growthRepository;
    private final DiaryService diaryService;

    @Transactional(readOnly = true)
    public GrowthVoListDto getGrowthListByUser(User user) {
        return GrowthVoListDto.builder()
            .growths(growthRepository.findByUser(user)
                .stream()
                .map(GrowthVo::from)
                .toList()
            )
            .build();
    }

    @Transactional
    public void createGrowth(User user, GrowthIntroduceRequest request) {
        Diary before = diaryService.getById(request.beforeDiaryId());
        Diary after = diaryService.getById(request.afterDiaryId());
        if (!before.getPlant().equals(after.getPlant())) {
            throw new IllegalArgumentException("The diaries do not belong to the same plant.");
        }

        growthRepository.save(
            Growth.builder()
                .beforeDiary(before)
                .afterDiary(after)
                .build()
        );
    }

    public void deleteGrowth(Long growthId) {
        growthRepository.deleteById(growthId);
    }
}
