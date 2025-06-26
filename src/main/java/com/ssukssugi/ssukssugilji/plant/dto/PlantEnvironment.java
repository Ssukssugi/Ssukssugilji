package com.ssukssugi.ssukssugilji.plant.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class PlantEnvironment {

    @Min(1)
    @Max(4)
    private Short shine;
    private Place place;
}
