package com.ssukssugi.ssukssugilji.plant.dao;

import com.ssukssugi.ssukssugilji.plant.entity.Diary;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface DiaryRepository extends CrudRepository<Diary, Long> {

    Optional<Diary> findTopByPlantIdOrderByCreatedAtDesc(Long plantId);

    List<Diary> findByPlantId(Long plantId);
}
