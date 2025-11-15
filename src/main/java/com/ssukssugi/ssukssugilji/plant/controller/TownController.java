package com.ssukssugi.ssukssugilji.plant.controller;

import com.ssukssugi.ssukssugilji.plant.dto.GrowthVoListDto;
import com.ssukssugi.ssukssugilji.plant.service.TownApplication;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/town")
@RequiredArgsConstructor
public class TownController {

    private final TownApplication townApplication;

    @GetMapping("/growth")
    public ResponseEntity<GrowthVoListDto> getGrowthList(
        @RequestParam(required = false) @Nullable Long lastGrowthId) {
        return ResponseEntity.ok(townApplication.getGrowthList(lastGrowthId));
    }

    @PostMapping("/growth/report")
    public ResponseEntity<Boolean> reportGrowth(@RequestParam Long growthId) {
        townApplication.reportGrowth(growthId);
        return ResponseEntity.ok(true);
    }
}
