package com.ssukssugi.ssukssugilji.plant.dao;

import com.ssukssugi.ssukssugilji.plant.entity.PlantCategory;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface PlantCategoryRepository extends CrudRepository<PlantCategory, Long> {

    List<PlantCategory> findByNameContaining(String keyword);
}
