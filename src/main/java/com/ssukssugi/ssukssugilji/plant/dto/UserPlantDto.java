package com.ssukssugi.ssukssugilji.plant.dto;

import com.ssukssugi.ssukssugilji.plant.entity.Plant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPlantDto {

    private Long plantId;
    private String name;
    private String plantCategory;
    private String image;
    private Long diaryCount;

    public static UserPlantDto fromEntity(Plant entity) {
        return UserPlantDto.builder()
            .plantId(entity.getPlantId())
            .name(entity.getName())
            .plantCategory(entity.getPlantCategory())
            .build();
    }
}
