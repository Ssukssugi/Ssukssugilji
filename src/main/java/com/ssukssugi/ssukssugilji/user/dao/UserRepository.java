package com.ssukssugi.ssukssugilji.user.dao;

import com.ssukssugi.ssukssugilji.user.dto.LoginType;
import com.ssukssugi.ssukssugilji.user.entity.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findBySocialIdAndLoginType(String socialId, LoginType loginType);

    Optional<User> findByNickname(String nickname);
}
