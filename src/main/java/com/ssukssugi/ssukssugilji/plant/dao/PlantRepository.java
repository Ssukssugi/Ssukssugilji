package com.ssukssugi.ssukssugilji.plant.dao;

import com.ssukssugi.ssukssugilji.plant.entity.Plant;
import com.ssukssugi.ssukssugilji.user.entity.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface PlantRepository extends CrudRepository<Plant, Long>, PlantRepositoryCustom {

    List<Plant> findByUser(User user);
}
