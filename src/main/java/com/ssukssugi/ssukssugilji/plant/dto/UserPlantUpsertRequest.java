package com.ssukssugi.ssukssugilji.plant.dto;

import lombok.Data;

@Data
public class UserPlantUpsertRequest {

    private String name;
    private String plantCategory;
    private PlantEnvironment plantEnvironment;
}
