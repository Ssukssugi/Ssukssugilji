package com.ssukssugi.ssukssugilji.plant.controller;

import com.ssukssugi.ssukssugilji.plant.dto.DiaryByMonthListDto;
import com.ssukssugi.ssukssugilji.plant.dto.DiaryCreateRequest;
import com.ssukssugi.ssukssugilji.plant.dto.DiaryUpdateRequest;
import com.ssukssugi.ssukssugilji.plant.service.DiaryService;
import com.ssukssugi.ssukssugilji.plant.service.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final PlantService plantService;
    private final DiaryService diaryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> createDiary(
        @RequestPart("request") DiaryCreateRequest request,
        @RequestPart("plantImage") MultipartFile image) {
        plantService.createDiary(request, image);
        return ResponseEntity.ok(true);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> updateDiary(
        @RequestParam("diaryId") Long diaryId,
        @RequestPart("request") DiaryUpdateRequest request,
        @RequestPart("plantImage") MultipartFile image) {
        plantService.updateDiary(diaryId, request, image);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/by-month")
    public ResponseEntity<DiaryByMonthListDto> getDiaryListByMonth(
        @RequestParam("plantId") Long plantId) {
        return ResponseEntity.ok(plantService.getDiaryListByMonth(plantId));
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteDiary(@RequestParam("diaryId") Long diaryId) {
        diaryService.deleteDiary(diaryId);
        return ResponseEntity.ok(true);
    }
}
