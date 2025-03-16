package com.ssukssugi.ssukssugilji.plant.controller;

import com.ssukssugi.ssukssugilji.plant.dto.PlantDiaryCreateRequest;
import com.ssukssugi.ssukssugilji.plant.service.PlantDiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/plant-diaries")
@RequiredArgsConstructor
public class PlantDiaryController {

    private final PlantDiaryService plantDiaryService;

    @PostMapping
    public ResponseEntity<Boolean> createPlantDiary(@RequestBody PlantDiaryCreateRequest request) {
        plantDiaryService.createPlantDiary(request);
        return ResponseEntity.ok(true);
    }
}
