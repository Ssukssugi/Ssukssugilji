package com.ssukssugi.ssukssugilji.plant.service;

import com.ssukssugi.ssukssugilji.common.UserContext;
import com.ssukssugi.ssukssugilji.plant.dto.GrowthVoListDto;
import com.ssukssugi.ssukssugilji.plant.entity.Growth;
import com.ssukssugi.ssukssugilji.user.entity.User;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TownApplication {

    private final GrowthService growthService;
    private final GrowthReportService growthReportService;

    public GrowthVoListDto getGrowthList(@Nullable Long cursorGrowthId) {
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

    public void reportGrowth(Long growthId) {
        User user = UserContext.getUser();
        Growth growth = growthService.findById(growthId);
        growthReportService.createReport(user, growth);
    }
}
