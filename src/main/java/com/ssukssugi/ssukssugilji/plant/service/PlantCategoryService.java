package com.ssukssugi.ssukssugilji.plant.service;

import com.ssukssugi.ssukssugilji.plant.dao.PlantCategoryRepository;
import com.ssukssugi.ssukssugilji.plant.dto.PlantCategoryDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlantCategoryService {

    private final PlantCategoryRepository plantCategoryRepository;

    public List<PlantCategoryDto> searchPlantCategory(String keyword) {
        return plantCategoryRepository.search(keyword)
            .stream()
            .map(PlantCategoryDto::fromEntity)
            .collect(Collectors.toList());
    }
}
