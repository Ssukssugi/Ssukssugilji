package com.ssukssugi.ssukssugilji.plant.dto;

import lombok.Data;

@Data
public class UserPlantCreateRequest {

    private String name;
    private String plantCategory;
    private PlantEnvironment plantEnvironment;
}
