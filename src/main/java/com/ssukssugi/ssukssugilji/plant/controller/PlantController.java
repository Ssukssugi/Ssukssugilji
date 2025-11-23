package com.ssukssugi.ssukssugilji.plant.controller;

import com.ssukssugi.ssukssugilji.common.UserContext;
import com.ssukssugi.ssukssugilji.plant.controller.dto.PlantCreateResponse;
import com.ssukssugi.ssukssugilji.plant.controller.dto.UserPlantUpsertRequest;
import com.ssukssugi.ssukssugilji.plant.dto.PlantProfileDto;
import com.ssukssugi.ssukssugilji.plant.dto.UserPlantDto;
import com.ssukssugi.ssukssugilji.plant.dto.UserPlantListDto;
import com.ssukssugi.ssukssugilji.plant.service.PlantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/plants")
public class PlantController {

    private final PlantService plantService;

    @GetMapping
    public ResponseEntity<UserPlantListDto> getUserPlantList(
        @RequestParam(required = false, defaultValue = "false") Boolean diaryCount) {
        UserPlantListDto response = new UserPlantListDto();
        response.setPlants(plantService.getUserPlantList(UserContext.getUser(), diaryCount));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<PlantProfileDto> getPlantProfile(@RequestParam("plantId") Long plantId) {
        return ResponseEntity.ok(plantService.getPlantProfile(plantId));
    }

    @PostMapping
    public ResponseEntity<PlantCreateResponse> createUserPlant(
        @Valid @RequestBody UserPlantUpsertRequest request) {
        return ResponseEntity.ok(new PlantCreateResponse(plantService.createPlant(request)));
    }

    @PutMapping
    public ResponseEntity<Boolean> updateUserPlant(
        @Valid @RequestBody UserPlantUpsertRequest request, @RequestParam("plantId") Long plantId) {
        plantService.updatePlant(plantId, request);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/{plantId}")
    public ResponseEntity<UserPlantDto> getUserPlantInfo(@PathVariable("plantId") Long plantId) {
        return ResponseEntity.ok(plantService.getUserPlantInfo(plantId));
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteUserPlant(@RequestParam("plantId") Long plantId) {
        plantService.deletePlant(plantId);
        return ResponseEntity.ok(true);
    }
}
