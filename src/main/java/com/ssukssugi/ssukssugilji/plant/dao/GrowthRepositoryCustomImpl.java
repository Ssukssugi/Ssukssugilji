package com.ssukssugi.ssukssugilji.plant.dao;

import static com.ssukssugi.ssukssugilji.plant.entity.QGrowth.growth;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssukssugi.ssukssugilji.plant.entity.Growth;
import jakarta.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GrowthRepositoryCustomImpl implements GrowthRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Growth> findNextGrowthPage(@Nullable Long cursorGrowthId, Integer pageSize) {
        if (cursorGrowthId == null) {
            return queryFactory
                .selectFrom(growth)
                .orderBy(growth.createdAt.desc(), growth.growthId.desc())
                .limit(pageSize)
                .fetch();
        }

        LocalDateTime cursorCreatedAt = queryFactory
            .select(growth.createdAt)
            .from(growth)
            .where(growth.growthId.eq(cursorGrowthId))
            .fetchOne();

        BooleanBuilder cursorCondition = new BooleanBuilder();
        cursorCondition.or(growth.createdAt.lt(cursorCreatedAt))
            .and(growth.growthId.ne(cursorGrowthId));

        return queryFactory
            .selectFrom(growth)
            .where(cursorCondition)
            .orderBy(growth.createdAt.desc(), growth.growthId.desc())
            .limit(pageSize)
            .fetch();
    }

    @Override
    public List<Growth> findRelatedGrowthsByDiaryId(Long diaryId) {
        return queryFactory
            .selectFrom(growth)
            .where(growth.beforeDiary.diaryId.eq(diaryId)
                .or(growth.afterDiary.diaryId.eq(diaryId)))
            .fetch();
    }
}
