package com.ssukssugi.ssukssugilji.auth.dao;

import com.ssukssugi.ssukssugilji.auth.entity.Token;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, Long> {

    Optional<Token> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
