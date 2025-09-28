package com.ssukssugi.ssukssugilji.plant.dao;

import com.ssukssugi.ssukssugilji.plant.dto.UserPlantDto;
import java.util.List;

public interface PlantRepositoryCustom {

    List<UserPlantDto> findPlantWithDiariesByUserId(Long userId);
}
