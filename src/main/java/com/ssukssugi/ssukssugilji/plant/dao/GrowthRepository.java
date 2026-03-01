package com.ssukssugi.ssukssugilji.plant.dao;

import com.ssukssugi.ssukssugilji.plant.entity.Growth;
import com.ssukssugi.ssukssugilji.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GrowthRepository extends JpaRepository<Growth, Long>, GrowthRepositoryCustom {

    List<Growth> findByUser(User user);

    @Modifying
    @Query("delete from Growth g where g.beforeDiary.diaryId = :diaryId or g.afterDiary.diaryId = :diaryId")
    void cleanGrowthsByDiaryId(@Param("diaryId") Long diaryId);
}
