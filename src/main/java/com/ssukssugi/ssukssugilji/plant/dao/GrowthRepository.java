package com.ssukssugi.ssukssugilji.plant.dao;

import com.ssukssugi.ssukssugilji.plant.entity.Growth;
import com.ssukssugi.ssukssugilji.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrowthRepository extends JpaRepository<Growth, Long> {

    Page<Growth> findByCreatedAtBeforeOrderByCreatedAtDesc(
        LocalDateTime cursorTime, Pageable pageable);

    List<Growth> findByUser(User user);
}
