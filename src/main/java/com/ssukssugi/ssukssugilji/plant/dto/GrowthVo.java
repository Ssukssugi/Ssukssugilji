package com.ssukssugi.ssukssugilji.plant.dto;

import com.ssukssugi.ssukssugilji.user.dto.profile.UserProfileDto;
import lombok.Builder;

@Builder
public record GrowthVo(

    Long growthId,
    UserProfileDto owner,
    PlantProfileDto plant,
    SimpleDiaryVo before,
    SimpleDiaryVo after
) {

}
