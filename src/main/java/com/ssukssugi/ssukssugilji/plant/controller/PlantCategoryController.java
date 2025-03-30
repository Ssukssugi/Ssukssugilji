package com.ssukssugi.ssukssugilji.plant.controller;

import com.ssukssugi.ssukssugilji.plant.service.PlantCategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/plant-categories")
public class PlantCategoryController {

    private final PlantCategoryService plantCategoryService;

    @GetMapping("/search")
    public ResponseEntity<List<String>> searchPlantCategory(@RequestParam String keyword) {
        return ResponseEntity.ok(plantCategoryService.searchPlantCategory(keyword));
    }
}
