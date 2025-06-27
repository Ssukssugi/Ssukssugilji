package com.ssukssugi.ssukssugilji.plant.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class PlantEnvironment {

    @Min(1)
    @Max(4)
    @Nullable
    private Short shine;
    @Nullable
    private Place place;
}
