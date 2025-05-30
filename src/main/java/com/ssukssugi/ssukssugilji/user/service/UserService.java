package com.ssukssugi.ssukssugilji.user.service;

import com.ssukssugi.ssukssugilji.auth.service.SecurityUtil;
import com.ssukssugi.ssukssugilji.user.dao.UserRepository;
import com.ssukssugi.ssukssugilji.user.dto.SocialAuthUserInfoDto;
import com.ssukssugi.ssukssugilji.user.dto.TermsAgreement;
import com.ssukssugi.ssukssugilji.user.dto.UserDetailDto;
import com.ssukssugi.ssukssugilji.user.entity.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserDetailService userDetailService;

    public Optional<User> getUserIdByAuthInfo(SocialAuthUserInfoDto socialAuthUserInfoDto) {
        return userRepository.findBySocialIdAndLoginType(
            socialAuthUserInfoDto.getSocialId(), socialAuthUserInfoDto.getLoginType());
    }

    @Transactional
    public User signUp(
        SocialAuthUserInfoDto socialAuthUserInfoDto, TermsAgreement termsAgreement) {
        User user = createUser(socialAuthUserInfoDto);
        user.setAgreeToReceiveMarketing(termsAgreement.isMarketing());
        return userRepository.save(user);
    }

    private User createUser(SocialAuthUserInfoDto socialAuthUserInfoDto) {
        return User.builder()
            .loginType(socialAuthUserInfoDto.getLoginType())
            .emailAddress(socialAuthUserInfoDto.getEmailAddress())
            .socialId(socialAuthUserInfoDto.getSocialId())
            .build();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("User not found, userId : %d", userId)));
    }

    public void saveUserDetail(UserDetailDto dto) {
        User user = SecurityUtil.getUser();
        if (userDetailService.findOptionalByUser(user).isPresent()) {
            throw new IllegalArgumentException("UserDetail is already exist");
        }
        userDetailService.createUserDetail(dto, user);
    }

    public Boolean checkNicknameExist(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    public Boolean checkIfUserInfoExist(Long userId) {
        return userDetailService.existByUser(findById(userId));
    }

    public void withdraw(User user) {
        userRepository.delete(user);
        userDetailService.deleteByUserIfExist(user);
    }
}
