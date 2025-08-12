package com.ssukssugi.ssukssugilji.plant.dto;

import com.ssukssugi.ssukssugilji.common.R2Util;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlantCategorySearchResultDto {

    private String name;
    private String imageUrl;

    public static PlantCategorySearchResultDto from(String name) {
        return PlantCategorySearchResultDto.builder()
            .name(name)
            .imageUrl(R2Util.toR2Url("/plant_dictionary/" + name + ".jpg"))
            .build();
    }
}
