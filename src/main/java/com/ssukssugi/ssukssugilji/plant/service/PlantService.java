package com.ssukssugi.ssukssugilji.plant.service;

import com.ssukssugi.ssukssugilji.common.R2Util;
import com.ssukssugi.ssukssugilji.common.UserContext;
import com.ssukssugi.ssukssugilji.plant.controller.dto.DiaryCreateRequest;
import com.ssukssugi.ssukssugilji.plant.controller.dto.DiaryUpdateRequest;
import com.ssukssugi.ssukssugilji.plant.controller.dto.UserPlantUpsertRequest;
import com.ssukssugi.ssukssugilji.plant.dao.PlantRepository;
import com.ssukssugi.ssukssugilji.plant.dto.DiaryByMonthListDto;
import com.ssukssugi.ssukssugilji.plant.dto.PlantProfileDto;
import com.ssukssugi.ssukssugilji.plant.dto.UserPlantDto;
import com.ssukssugi.ssukssugilji.plant.entity.Diary;
import com.ssukssugi.ssukssugilji.plant.entity.Plant;
import com.ssukssugi.ssukssugilji.user.entity.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PlantService {

    private final PlantRepository plantRepository;
    private final DiaryService diaryService;

    public List<UserPlantDto> getUserPlantList(User user, boolean addDiaryCount) {
        List<UserPlantDto> plantDtoList = plantRepository.findPlantWithDiariesByUserId(
            user.getUserId());
        attachDiaryCount(addDiaryCount, plantDtoList);
        return plantDtoList;
    }

    private void attachDiaryCount(boolean addDiaryCount, List<UserPlantDto> plantDtoList) {
        if (!addDiaryCount) {
            return;
        }
        List<Long> plantIds = plantDtoList.stream().map(UserPlantDto::getPlantId).toList();

        // TODO: move responsibility to diaryService
        plantRepository.getDiaryCounts(plantIds).forEach((plantId, count) -> {
            plantDtoList.stream()
                .filter(dto -> dto.getPlantId().equals(plantId))
                .findFirst()
                .ifPresent(plantDto -> plantDto.setDiaryCount(count));
        });
    }

    public UserPlantDto getUserPlantInfo(Long plantId) {
        return UserPlantDto.fromEntity(getById(plantId));
    }

    public Plant getById(Long plantId) {
        return plantRepository.findById(plantId)
            .orElseThrow(
                () -> new IllegalArgumentException("Plant not found, plantId = " + plantId));
    }

    @Transactional
    public Long createPlant(UserPlantUpsertRequest request) {
        Plant saved = plantRepository.save(Plant.builder()
            .name(request.getName())
            .plantCategory(request.getPlantCategory())
            .shine(request.getPlantEnvironment().getShine())
            .place(request.getPlantEnvironment().getPlace())
            .user(UserContext.getUser())
            .build());
        return saved.getPlantId();
    }

    public PlantProfileDto getPlantProfile(Long plantId) {
        Plant plant = getById(plantId);
        Optional<Diary> mostRecent = diaryService.getMostRecent(plant);
        return PlantProfileDto.builder()
            .name(plant.getName())
            .plantCategory(plant.getPlantCategory())
            .plantImage(mostRecent.map(Diary::getImageUrl).map(R2Util::toR2Url).orElse(null))
            .shine(plant.getShine())
            .place(plant.getPlace())
            .build();
    }

    public DiaryByMonthListDto getDiaryListByMonth(Long plantId) {
        return diaryService.getDiaryListByMonth(getById(plantId));
    }

    public Diary createDiary(DiaryCreateRequest request, MultipartFile image) {
        return diaryService.createDiary(request, getById(request.getPlantId()), image);
    }

    public void updateDiary(Long diaryId, DiaryUpdateRequest request, MultipartFile image) {
        Plant plant = getById(request.getPlantId());
        diaryService.updateDiary(plant, diaryId, request, image);
    }

    public void deletePlant(Long plantId) {
        plantRepository.deleteById(plantId);
    }

    @Transactional
    public void updatePlant(Long plantId, UserPlantUpsertRequest request) {
        Plant plant = getById(plantId);
        plant.setName(request.getName());
        plant.setPlantCategory(request.getPlantCategory());
        plant.setShine(request.getPlantEnvironment().getShine());
        plant.setPlace(request.getPlantEnvironment().getPlace());
    }
}
