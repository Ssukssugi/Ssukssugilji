package com.ssukssugi.ssukssugilji.plant.service;

import com.ssukssugi.ssukssugilji.plant.dao.GrowthRepository;
import com.ssukssugi.ssukssugilji.plant.dto.GrowthIntroduceRequest;
import com.ssukssugi.ssukssugilji.plant.dto.GrowthVo;
import com.ssukssugi.ssukssugilji.plant.dto.GrowthVoListDto;
import com.ssukssugi.ssukssugilji.plant.dto.SimpleDiaryVo;
import com.ssukssugi.ssukssugilji.plant.entity.Diary;
import com.ssukssugi.ssukssugilji.plant.entity.Growth;
import com.ssukssugi.ssukssugilji.user.dto.profile.UserProfileDto;
import com.ssukssugi.ssukssugilji.user.entity.User;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    private Growth findById(Long growthId) {
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
                .nickname(growth.getUser().getNickname())
                .build())
            .plant(plantService.getPlantProfile(growth.getBeforeDiary().getPlant().getPlantId()))
            .before(SimpleDiaryVo.from(growth.getBeforeDiary()))
            .after(SimpleDiaryVo.from(growth.getAfterDiary()))
            .build();
    }

    public void createGrowth(User user, GrowthIntroduceRequest request) {
        Diary before = diaryService.getById(request.beforeDiaryId());
        Diary after = diaryService.getById(request.afterDiaryId());
        if (!before.getPlant().equals(after.getPlant())) {
            throw new IllegalArgumentException("The diaries do not belong to the same plant.");
        }

        growthRepository.save(
            Growth.builder()
                .user(user)
                .beforeDiary(before)
                .afterDiary(after)
                .build()
        );
    }

    public void deleteGrowth(Long growthId) {
        growthRepository.deleteById(growthId);
    }

    @Transactional(readOnly = true)
    public Page<Growth> getGrowthListPage(Long cursorGrowthId) {
        if (cursorGrowthId == 0L) {
            return growthRepository.findByCreatedAtBeforeOrderByCreatedAtDesc(
                LocalDateTime.now(),
                PageRequest.of(0, PAGE_SIZE));
        }

        LocalDateTime cursorCreatedAt = findById(cursorGrowthId).getCreatedAt();
        return growthRepository.findByCreatedAtBeforeOrderByCreatedAtDesc(
            cursorCreatedAt, PageRequest.of(0, PAGE_SIZE));
    }
}
