package com.ssukssugi.ssukssugilji.plant.controller.dto;

import com.ssukssugi.ssukssugilji.plant.dto.PlantEnvironment;
import lombok.Data;

@Data
public class UserPlantUpsertRequest {

    private String name;
    private String plantCategory;
    private PlantEnvironment plantEnvironment;
}
