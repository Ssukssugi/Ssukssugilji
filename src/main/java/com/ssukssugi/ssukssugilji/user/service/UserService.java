package com.ssukssugi.ssukssugilji.user.service;

import com.ssukssugi.ssukssugilji.common.UserContext;
import com.ssukssugi.ssukssugilji.user.dao.UserRepository;
import com.ssukssugi.ssukssugilji.user.dto.SignUpRequest;
import com.ssukssugi.ssukssugilji.user.dto.SocialAuthUserInfoDto;
import com.ssukssugi.ssukssugilji.user.dto.UserDetailDto;
import com.ssukssugi.ssukssugilji.user.dto.profile.UserProfileDto;
import com.ssukssugi.ssukssugilji.user.dto.profile.UserProfileUpdateRequest;
import com.ssukssugi.ssukssugilji.user.dto.setting.UserSettingDto;
import com.ssukssugi.ssukssugilji.user.dto.setting.UserToggleSetRequest;
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

    public Optional<User> getUserOptByAuthInfo(SocialAuthUserInfoDto socialAuthUserInfoDto) {
        return userRepository.findBySocialIdAndLoginType(
            socialAuthUserInfoDto.getSocialId(), socialAuthUserInfoDto.getLoginType());
    }

    @Transactional
    public User signUp(SignUpRequest request) {
        User user = createUser(request);
        user.setAgreeToReceiveMarketing(request.getTermsAgreement().isMarketing());
        return userRepository.save(user);
    }

    private User createUser(SignUpRequest request) {
        return User.builder()
            .loginType(request.getLoginType())
            .socialId(request.getSocialId())
            .emailAddress(request.getEmailAddress())
            .receiveServiceNoti(true)
            .build();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("User not found, userId : %d", userId)));
    }

    public void saveUserDetail(UserDetailDto dto) {
        User user = UserContext.getUser();
        if (userDetailService.findOptionalByUser(user).isPresent()) {
            throw new IllegalArgumentException("UserDetail is already exist");
        }
        userDetailService.createUserDetail(dto, user);
    }

    public Boolean checkNicknameExist(String nickname) {
        return userDetailService.findByNickName(nickname);
    }

    public Boolean checkIfUserInfoExist(Long userId) {
        return userDetailService.existByUser(findById(userId));
    }

    public void withdraw(User user) {
        userRepository.delete(user);
        userDetailService.deleteByUserIfExist(user);
    }

    public UserProfileDto getUserProfile() {
        return UserProfileDto.builder()
            .nickname(UserContext.getUser().getUserDetail().getNickname())
            .build();
    }

    public void updateUserProfile(UserProfileUpdateRequest request) {
        User user = UserContext.getUser();
        userDetailService.updateNickName(user, request.getNickname());
    }

    public UserSettingDto getUserSettings() {
        return UserSettingDto.fromEntity(UserContext.getUser());
    }

    public void setUserToggle(UserToggleSetRequest request) {
        User user = UserContext.getUser();
        switch (request.getKey()) {
            case MARKETING -> user.setAgreeToReceiveMarketing(request.getValue());
            case SERVICE_NOTI -> user.setReceiveServiceNoti(request.getValue());
            default -> throw new RuntimeException("Invalid User Toggle Key: " + request.getKey());
        }
        userRepository.save(user);
    }
}
