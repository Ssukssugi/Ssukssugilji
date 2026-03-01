package com.ssukssugi.ssukssugilji.plant.controller;

import com.ssukssugi.ssukssugilji.common.UserContext;
import com.ssukssugi.ssukssugilji.plant.controller.dto.GrowthIntroduceRequest;
import com.ssukssugi.ssukssugilji.plant.dto.GrowthVoListDto;
import com.ssukssugi.ssukssugilji.plant.service.GrowthApplication;
import com.ssukssugi.ssukssugilji.plant.service.GrowthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/growth")
@RequiredArgsConstructor
public class GrowthController {

    private final GrowthApplication growthApplication;
    // TODO: delete growthService injection here
    private final GrowthService growthService;

    @GetMapping
    public ResponseEntity<GrowthVoListDto> getMyGrowthList() {
        return ResponseEntity.ok(growthService.getGrowthListByUser(UserContext.getUser()));
    }

    @PostMapping
    public ResponseEntity<Boolean> introduceGrowth(
        @RequestBody @Valid GrowthIntroduceRequest request) {
        growthApplication.createGrowth(UserContext.getUser(), request);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteGrowth(@RequestParam Long growthId) {
        growthService.deleteGrowth(growthId);
        return ResponseEntity.ok(true);
    }
}
