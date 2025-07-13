package com.ssukssugi.ssukssugilji.plant.service;

import com.ssukssugi.ssukssugilji.auth.service.SecurityUtil;
import com.ssukssugi.ssukssugilji.common.BaseEntity;
import com.ssukssugi.ssukssugilji.plant.dao.PlantRepository;
import com.ssukssugi.ssukssugilji.plant.dto.PlantProfileDto;
import com.ssukssugi.ssukssugilji.plant.dto.UserPlantCreateRequest;
import com.ssukssugi.ssukssugilji.plant.dto.UserPlantDto;
import com.ssukssugi.ssukssugilji.plant.entity.Diary;
import com.ssukssugi.ssukssugilji.plant.entity.Plant;
import com.ssukssugi.ssukssugilji.user.entity.User;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlantService {

    private final PlantRepository plantRepository;
    private final DiaryService diaryService;

    public List<UserPlantDto> getUserPlantList(User user) {
        return plantRepository.findByUser(user)
            .stream()
            .sorted(Comparator.comparing(BaseEntity::getCreatedAt).reversed())
            .map(UserPlantDto::fromEntity)
            .collect(Collectors.toList());
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
    public void createPlant(UserPlantCreateRequest request) {
        plantRepository.save(Plant.builder()
            .name(request.getName())
            .plantCategory(request.getPlantCategory())
            .shine(request.getPlantEnvironment().getShine())
            .place(request.getPlantEnvironment().getPlace())
            .user(SecurityUtil.getUser())
            .build());
    }

    public PlantProfileDto getPlantProfile(Long plantId) {
        Plant entity = getById(plantId);
        Optional<Diary> mostRecent = diaryService.getMostRecent(plantId);
        return PlantProfileDto.builder()
            .name(entity.getName())
            .plantCategory(entity.getPlantCategory())
            .plantImage(mostRecent.map(Diary::getImageUrl).orElse(null))
            .shine(entity.getShine())
            .build();
    }
}
