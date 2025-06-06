package com.ssukssugi.ssukssugilji.plant.controller;

import com.ssukssugi.ssukssugilji.plant.dto.DiaryCreateRequest;
import com.ssukssugi.ssukssugilji.plant.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<Boolean> createDiary(@RequestBody DiaryCreateRequest request) {
        diaryService.createDiary(request);
        return ResponseEntity.ok(true);
    }
}
