package com.ssukssugi.ssukssugilji.plant.dao;

import com.ssukssugi.ssukssugilji.plant.dto.UserPlantDto;
import java.util.List;
import java.util.Map;

public interface PlantRepositoryCustom {

    List<UserPlantDto> findPlantWithDiariesByUserId(Long userId);

    Map<Long, Long> getDiaryCounts(List<Long> plantIds);
}
