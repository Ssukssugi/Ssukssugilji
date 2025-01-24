package com.ssukssugi.ssukssugilji.user.application;

import com.ssukssugi.ssukssugilji.auth.service.JwtService;
import com.ssukssugi.ssukssugilji.user.dto.LoginType;
import com.ssukssugi.ssukssugilji.user.dto.SignUpRequest;
import com.ssukssugi.ssukssugilji.user.dto.SocialAuthUserInfoDto;
import com.ssukssugi.ssukssugilji.user.dto.SocialLoginRequest;
import com.ssukssugi.ssukssugilji.user.dto.SocialLoginResponse;
import com.ssukssugi.ssukssugilji.user.entity.User;
import com.ssukssugi.ssukssugilji.user.service.UserService;
import com.ssukssugi.ssukssugilji.user.service.auth.SocialAuthContext;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialLoginApplication {

    private final SocialAuthContext socialAuthContext;
    private final UserService userService;
    private final JwtService jwtService;

    private final Long USER_ID_NOT_FOUND = -1L;

    public SocialLoginResponse socialLogin(
        SocialLoginRequest request, HttpServletResponse response) {

        Long userId = findUserIdByAuthInfo(request.getLoginType(), request.getAccessToken());
        boolean isRegistered = !Objects.equals(userId, USER_ID_NOT_FOUND);
        if (isRegistered) {
            setTokenHeader(response, userId);
        }
        return SocialLoginResponse
            .builder()
            .isRegistered(isRegistered)
            // add existInfo
            .build();
    }

    private Long findUserIdByAuthInfo(LoginType loginType, String accessToken) {
        SocialAuthUserInfoDto socialAuthUserInfoDto = socialAuthContext
            .getAuthUserInfo(loginType, accessToken);
        return userService.getUserIdByAuthInfo(socialAuthUserInfoDto)
            .map(User::getUserId)
            .orElse(USER_ID_NOT_FOUND);
    }

    public void signUp(SignUpRequest request, HttpServletResponse response) {
        SocialAuthUserInfoDto socialAuthUserInfoDto = socialAuthContext
            .getAuthUserInfo(request.getLoginType(), request.getAccessToken());
        Long userId = userService.signUp(socialAuthUserInfoDto, request.getTermsAgreement());
        setTokenHeader(response, userId);
    }

    private void setTokenHeader(HttpServletResponse response, Long userId) {
        jwtService.generateAccessToken(response, userId);
        jwtService.generateRefreshToken(response, userId);
    }
}
