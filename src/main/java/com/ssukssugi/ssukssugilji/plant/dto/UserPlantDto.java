package com.ssukssugi.ssukssugilji.plant.dto;

import com.ssukssugi.ssukssugilji.plant.entity.Plant;
import lombok.Builder;

@Builder
public class UserPlantDto {

    private Long plantId;
    private String name;
    private String plantCategory;

    public static UserPlantDto fromEntity(Plant entity) {
        return UserPlantDto.builder()
            .plantId(entity.getPlantId())
            .name(entity.getName())
            .plantCategory(entity.getPlantCategory())
            .build();
    }
}
