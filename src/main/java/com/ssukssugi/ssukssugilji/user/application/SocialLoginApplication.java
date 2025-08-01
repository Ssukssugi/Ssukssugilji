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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocialLoginApplication {

    private final SocialAuthContext socialAuthContext;
    private final UserService userService;
    private final JwtService jwtService;

    public SocialLoginResponse socialLogin(
        SocialLoginRequest request, HttpServletResponse response) {
        Optional<User> user = findUserIdByAuthInfo(
            request.getLoginType(), request.getAccessToken());
        boolean isRegistered = user.isPresent();
        boolean existInfo = false;
        if (isRegistered) {
            Long userId = user.get().getUserId();
            setTokenHeader(response, userId);
            existInfo = userService.checkIfUserInfoExist(userId);
            log.info("success to social-login, userId: {}, existInfo: {}", userId, existInfo);
        }

        return SocialLoginResponse
            .builder()
            .isRegistered(isRegistered)
            .existInfo(existInfo)
            .build();
    }

    private Optional<User> findUserIdByAuthInfo(LoginType loginType, String accessToken) {
        SocialAuthUserInfoDto socialAuthUserInfoDto = socialAuthContext
            .getAuthUserInfo(loginType, accessToken);
        return userService.getUserIdByAuthInfo(socialAuthUserInfoDto);
    }

    @Transactional
    public void signUp(SignUpRequest request, HttpServletResponse response) {
        SocialAuthUserInfoDto socialAuthUserInfoDto = socialAuthContext
            .getAuthUserInfo(request.getLoginType(), request.getAccessToken());
        Long userId = userService.signUp(socialAuthUserInfoDto, request.getTermsAgreement())
            .getUserId();
        log.info("Success to sign-up, userId: {}, loginType: {}", userId, request.getLoginType());
        setTokenHeader(response, userId);
    }

    private void setTokenHeader(HttpServletResponse response, Long userId) {
        jwtService.issueAndSetAccessToken(response, userId);
        jwtService.issueAndSetRefreshToken(response, userId);
    }
}
