package com.ssukssugi.ssukssugilji.plant.dao;

import com.ssukssugi.ssukssugilji.plant.entity.PlantCategory;
import java.util.List;

public interface PlantCategoryRepositoryCustom {

    List<PlantCategory> search(String keyword);
}
