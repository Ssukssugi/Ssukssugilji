package com.ssukssugi.ssukssugilji.plant.controller.dto;

import lombok.Data;

@Data
public class PlantCreateResponse {

    private Long plantId;

    public PlantCreateResponse(Long plantId) {
        this.plantId = plantId;
    }
}
