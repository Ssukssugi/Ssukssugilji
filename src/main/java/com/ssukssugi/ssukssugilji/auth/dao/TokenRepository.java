package com.ssukssugi.ssukssugilji.auth.dao;

import com.ssukssugi.ssukssugilji.auth.entity.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, Long> {

    public Token findByUserId(Long userId);

    public void deleteByUserId(Long userId);
}
