package com.ssukssugi.ssukssugilji.plant.dao;

import com.ssukssugi.ssukssugilji.plant.entity.Growth;
import com.ssukssugi.ssukssugilji.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrowthRepository extends JpaRepository<Growth, Long>, GrowthRepositoryCustom {

    List<Growth> findByUser(User user);
}
