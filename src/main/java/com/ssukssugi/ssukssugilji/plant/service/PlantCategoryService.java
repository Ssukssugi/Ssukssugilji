package com.ssukssugi.ssukssugilji.plant.service;

import com.ssukssugi.ssukssugilji.plant.dao.PlantCategoryRepository;
import com.ssukssugi.ssukssugilji.plant.entity.PlantCategory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlantCategoryService {

    private final PlantCategoryRepository plantCategoryRepository;

    public List<String> searchPlantCategory(String keyword) {
        return plantCategoryRepository.findByNameContaining(keyword)
            .stream()
            .map(PlantCategory::getName)
            .collect(Collectors.toList());
    }
}
