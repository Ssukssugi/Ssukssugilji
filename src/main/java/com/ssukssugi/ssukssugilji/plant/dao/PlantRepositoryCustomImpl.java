package com.ssukssugi.ssukssugilji.plant.dao;

import static com.ssukssugi.ssukssugilji.plant.entity.QDiary.diary1;
import static com.ssukssugi.ssukssugilji.plant.entity.QPlant.plant;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssukssugi.ssukssugilji.common.R2Util;
import com.ssukssugi.ssukssugilji.plant.dto.UserPlantDto;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlantRepositoryCustomImpl implements PlantRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserPlantDto> findPlantWithDiariesByUserId(Long userId) {
        List<UserPlantDto> results = queryFactory
            .select(Projections.fields(UserPlantDto.class,
                plant.plantId,
                plant.name,
                plant.plantCategory,
                diary1.imageUrl.as("image")
            ))
            .from(plant)
            .leftJoin(diary1).on(diary1.plant.eq(plant)
                .and(diary1.createdAt.eq(
                    JPAExpressions
                        .select(diary1.createdAt.max())
                        .from(diary1)
                        .where(diary1.plant.eq(plant))
                )))
            .where(plant.user.userId.eq(userId))
            .orderBy(plant.createdAt.desc())
            .fetch();

        results.forEach(dto -> {
            if (dto.getImage() != null) {
                dto.setImage(R2Util.toR2Url(dto.getImage()));
            }
        });

        return results;
    }
}
