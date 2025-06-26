package com.ssukssugi.ssukssugilji.plant.controller;

import com.ssukssugi.ssukssugilji.auth.service.SecurityUtil;
import com.ssukssugi.ssukssugilji.plant.dto.UserPlantCreateRequest;
import com.ssukssugi.ssukssugilji.plant.dto.UserPlantDto;
import com.ssukssugi.ssukssugilji.plant.dto.UserPlantListDto;
import com.ssukssugi.ssukssugilji.plant.service.PlantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/plants")
public class PlantController {

    private final PlantService plantService;

    @GetMapping
    public ResponseEntity<UserPlantListDto> getUserPlantList() {
        UserPlantListDto response = new UserPlantListDto();
        response.setPlants(plantService.getUserPlantList(SecurityUtil.getUser()));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Boolean> createUserPlant(
        @Valid @RequestBody UserPlantCreateRequest request) {
        plantService.createPlant(request);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/{plantId}")
    public ResponseEntity<UserPlantDto> getUserPlantInfo(@PathVariable("plantId") Long plantId) {
        return ResponseEntity.ok(plantService.getUserPlantInfo(plantId));
    }
}
