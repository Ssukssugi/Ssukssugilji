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
            .nickname(dto.getNickname())
            .ageGroup(dto.getAgeGroup())
            .plantReason(dto.getPlantReason())
            .signUpPath(dto.getSignUpPath())
            .build());
    }

    private UserDetail findByUser(User user) {
        return findOptionalByUser(user)
            .orElseThrow(() -> new IllegalArgumentException("UserDetail not found, user: " + user));
    }

    public Optional<UserDetail> findOptionalByUser(User user) {
        return userDetailRepository.findByUser(user);
    }

    public Boolean existByUser(User user) {
        return findOptionalByUser(user).isPresent();
    }

    public void deleteByUserIfExist(User user) {
        if (existByUser(user)) {
            userDetailRepository.delete(findByUser(user));
        }
    }

    public Boolean findByNickName(String nickname) {
        return userDetailRepository.findByNickname(nickname).isPresent();
    }

    public void updateNickName(User user, String nickName) {
        UserDetail userDetail = findByUser(user);
        userDetail.setNickname(nickName);
        userDetailRepository.save(userDetail);
    }
}
