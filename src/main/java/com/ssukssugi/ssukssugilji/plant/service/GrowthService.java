package com.ssukssugi.ssukssugilji.plant.service;

import com.ssukssugi.ssukssugilji.plant.dao.GrowthRepository;
import com.ssukssugi.ssukssugilji.plant.dto.GrowthVo;
import com.ssukssugi.ssukssugilji.plant.dto.GrowthVoListDto;
import com.ssukssugi.ssukssugilji.plant.dto.SimpleDiaryVo;
import com.ssukssugi.ssukssugilji.plant.entity.Diary;
import com.ssukssugi.ssukssugilji.plant.entity.Growth;
import com.ssukssugi.ssukssugilji.user.dto.profile.UserProfileDto;
import com.ssukssugi.ssukssugilji.user.entity.User;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GrowthService {

    private static final Integer PAGE_SIZE = 5;
    private final GrowthRepository growthRepository;
    private final DiaryService diaryService;
    private final PlantService plantService;

    public Growth findById(Long growthId) {
        return growthRepository.findById(growthId)
            .orElseThrow(
                () -> new IllegalArgumentException("Growth not found with id: " + growthId));
    }

    @Transactional(readOnly = true)
    public GrowthVoListDto getGrowthListByUser(User user) {
        return GrowthVoListDto.builder()
            .growths(growthRepository.findByUser(user)
                .stream()
                .map(this::convertToGrowthVo)
                .toList()
            )
            .build();
    }

    public GrowthVo convertToGrowthVo(Growth growth) {
        return GrowthVo.builder()
            .growthId(growth.getGrowthId())
            .owner(UserProfileDto
                .builder()
                .userId(growth.getUser().getUserId())
                .nickname(growth.getUser().getUserDetail().getNickname())
                .build())
            .plant(plantService.getPlantProfile(growth.getBeforeDiary().getPlant().getPlantId()))
            .before(SimpleDiaryVo.from(growth.getBeforeDiary()))
            .after(SimpleDiaryVo.from(growth.getAfterDiary()))
            .build();
    }

    public void deleteGrowth(Long growthId) {
        growthRepository.deleteById(growthId);
    }

    @Transactional(readOnly = true)
    public Page<Growth> getGrowthListPage(@Nullable Long cursorGrowthId) {
        return new PageImpl<>(growthRepository.findNextGrowthPage(cursorGrowthId, PAGE_SIZE));
    }

    public void createEntity(User user, Diary before, Diary after) {
        growthRepository.save(
            Growth.builder()
                .user(user)
                .beforeDiary(before)
                .afterDiary(after)
                .build()
        );
    }

    public void cleanGrowthsByDiaryId(Long diaryId) {
        growthRepository.cleanGrowthsByDiaryId(diaryId);
    }
}
