package com.ssukssugi.ssukssugilji.plant.service;

import com.ssukssugi.ssukssugilji.plant.dto.GrowthVoListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TownApplication {

    private final GrowthService growthService;

    public GrowthVoListDto getGrowthList(Long cursorGrowthId) {
        return GrowthVoListDto.builder()
            .growths(
                growthService.getGrowthListPage(cursorGrowthId)
                    .getContent()
                    .stream()
                    .map(growthService::convertToGrowthVo)
                    .toList()
            )
            .build();
    }
}
