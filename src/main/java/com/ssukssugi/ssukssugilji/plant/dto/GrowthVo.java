package com.ssukssugi.ssukssugilji.plant.dto;

import com.ssukssugi.ssukssugilji.plant.entity.Growth;
import com.ssukssugi.ssukssugilji.user.dto.profile.UserProfileDto;
import lombok.Builder;

@Builder
public record GrowthVo(

    Long growthId,
    UserProfileDto owner,
    SimpleDiaryVo before,
    SimpleDiaryVo after
) {

    public static GrowthVo from(Growth growth) {
        return GrowthVo.builder()
            .growthId(growth.getGrowthId())
            .owner(UserProfileDto
                .builder()
                .userId(growth.getUser().getUserId())
                .build())
            .before(SimpleDiaryVo.from(growth.getBeforeDiary()))
            .after(SimpleDiaryVo.from(growth.getAfterDiary()))
            .build();
    }
}
