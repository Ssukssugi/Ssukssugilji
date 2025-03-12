package com.ssukssugi.ssukssugilji.user.service;

import com.ssukssugi.ssukssugilji.user.dao.UserDetailRepository;
import com.ssukssugi.ssukssugilji.user.dto.UserDetailDto;
import com.ssukssugi.ssukssugilji.user.entity.User;
import com.ssukssugi.ssukssugilji.user.entity.UserDetail;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailService {

    private final UserDetailRepository userDetailRepository;

    public void createUserDetail(UserDetailDto dto, User user) {
        userDetailRepository.save(UserDetail.builder()
            .user(user)
            .ageGroup(dto.getAgeGroup())
            .plantReason(dto.getPlantReason())
            .signUpPath(dto.getSignUpPath())
            .build());
    }

    public Optional<UserDetail> findByUser(User user) {
        return userDetailRepository.findByUser(user);
    }

    public Boolean existByUser(User user) {
        return findByUser(user).isPresent();
    }
}
