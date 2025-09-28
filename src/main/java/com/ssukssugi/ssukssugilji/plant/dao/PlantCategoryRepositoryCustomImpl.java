package com.ssukssugi.ssukssugilji.plant.dao;

import static com.ssukssugi.ssukssugilji.plant.entity.QPlantCategory.plantCategory;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssukssugi.ssukssugilji.plant.entity.PlantCategory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlantCategoryRepositoryCustomImpl implements PlantCategoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PlantCategory> search(String keyword) {
        return queryFactory
            .selectFrom(plantCategory)
            .where(plantCategory.name.contains(keyword))
            .orderBy(
                Expressions.numberTemplate(Integer.class,
                    "CHARINDEX({0}, {1})", Expressions.constant(keyword), plantCategory.name
                ).asc(),
                plantCategory.name.asc()
            )
            .fetch();
    }
}
