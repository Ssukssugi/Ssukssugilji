package com.ssukssugi.ssukssugilji.plant.service;

import com.ssukssugi.ssukssugilji.plant.dao.PlantCategoryRepository;
import com.ssukssugi.ssukssugilji.plant.dto.PlantCategorySearchResultDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlantCategoryService {

    private final PlantCategoryRepository plantCategoryRepository;

    public List<PlantCategorySearchResultDto> searchPlantCategory(String keyword) {
        return plantCategoryRepository.findByNameLike(wrap(keyword))
            .stream()
            .map(plantCategory -> PlantCategorySearchResultDto.from(plantCategory.getName()))
            .collect(Collectors.toList());
    }

    private String wrap(String str) {
        return "%" + str + "%";
    }
}
