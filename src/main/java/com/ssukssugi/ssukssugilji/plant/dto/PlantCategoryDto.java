package com.ssukssugi.ssukssugilji.plant.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssukssugi.ssukssugilji.common.R2Util;
import com.ssukssugi.ssukssugilji.plant.entity.PlantCategory;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlantCategoryDto {

    private String name;
    private String imageUrl;

    @JsonIgnore
    private static String PLANT_CATEGORY_DIR = "/plant_dictionary";

    public static PlantCategoryDto fromEntity(PlantCategory entity) {
        return PlantCategoryDto.builder()
            .name(entity.getName())
            .imageUrl(R2Util.toR2Url(PLANT_CATEGORY_DIR + "/" + entity.getName() + ".jpg"))
            .build();
    }
}
