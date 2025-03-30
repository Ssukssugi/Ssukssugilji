package com.ssukssugi.ssukssugilji.user.dao;

import com.ssukssugi.ssukssugilji.user.entity.User;
import com.ssukssugi.ssukssugilji.user.entity.UserDetail;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserDetailRepository extends CrudRepository<UserDetail, Long> {

    Optional<UserDetail> findByUser(User user);
}
