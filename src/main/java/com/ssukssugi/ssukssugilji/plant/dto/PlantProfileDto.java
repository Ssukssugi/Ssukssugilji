package com.ssukssugi.ssukssugilji.plant.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlantProfileDto {

    private String name;
    private String plantCategory;
    private String plantImage;
    private Short shine;
}
