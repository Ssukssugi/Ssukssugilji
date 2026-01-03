package com.ssukssugi.ssukssugilji.plant.dao;

import com.ssukssugi.ssukssugilji.plant.entity.Growth;
import jakarta.annotation.Nullable;
import java.util.List;

public interface GrowthRepositoryCustom {

    List<Growth> findNextGrowthPage(@Nullable Long cursorGrowthId, Integer pageSize);

    List<Growth> findRelatedGrowthsByDiaryId(Long plantId);
}
