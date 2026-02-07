package com.ssukssugi.ssukssugilji.plant.dao;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssukssugi.ssukssugilji.common.R2Util;
import com.ssukssugi.ssukssugilji.plant.dto.UserPlantDto;
import com.ssukssugi.ssukssugilji.plant.entity.QDiary;
import com.ssukssugi.ssukssugilji.plant.entity.QPlant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlantRepositoryCustomImpl implements PlantRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QPlant plant = QPlant.plant;
    private final QDiary diary = QDiary.diary1;

    @Override
    public List<UserPlantDto> findPlantWithDiariesByUserId(Long userId) {
        QDiary d1 = new QDiary("d1");
        QDiary d2 = new QDiary("d2");

        List<UserPlantDto> results = queryFactory
            .select(Projections.fields(UserPlantDto.class,
                plant.plantId,
                plant.name,
                plant.plantCategory,
                diary.imageUrl.as("image"),
                ExpressionUtils.as(Expressions.constant(0L), "diaryCount")
            ))
            .from(plant)
            .leftJoin(diary).on(
                diary.plant.eq(plant)
                    .and(diary.date.eq(
                        JPAExpressions
                            .select(d1.date.max())
                            .from(d1)
                            .where(d1.plant.eq(plant))
                    ))
                    .and(diary.createdAt.eq(
                        JPAExpressions
                            .select(d2.createdAt.max())
                            .from(d2)
                            .where(
                                d2.plant.eq(plant)
                                    .and(d2.date.eq(
                                        JPAExpressions
                                            .select(d1.date.max())
                                            .from(d1)
                                            .where(d1.plant.eq(plant))
                                    ))
                            )
                    ))
            )
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

    @Override
    public Map<Long, Long> getDiaryCounts(List<Long> plantIds) {
        if (plantIds == null || plantIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return queryFactory
            .select(diary.plant.plantId, diary.count())
            .from(diary)
            .where(diary.plant.plantId.in(plantIds))
            .groupBy(diary.plant.plantId)
            .fetch()
            .stream()
            .collect(Collectors.toMap(
                t -> t.get(diary.plant.plantId),
                t -> t.get(diary.count())
            ));
    }
}
