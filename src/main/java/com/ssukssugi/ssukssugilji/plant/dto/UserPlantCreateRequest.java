package com.ssukssugi.ssukssugilji.plant.dto;

import lombok.Data;

@Data
public class UserPlantCreateRequest {

    private String plantCategory;
    private String name;
    private PlantEnvironment plantEnvironment;
    private Boolean secret;
}
