package com.ssukssugi.ssukssugilji.user.application;

import com.ssukssugi.ssukssugilji.user.dto.SocialAuthUserInfoDto;
import com.ssukssugi.ssukssugilji.user.dto.SocialLoginRequest;
import com.ssukssugi.ssukssugilji.user.service.UserService;
import com.ssukssugi.ssukssugilji.user.service.auth.SocialAuthContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialLoginApplication {

    private final SocialAuthContext socialAuthContext;
    private final UserService userService;

    public Long signInOrSignUp(SocialLoginRequest request) {
        SocialAuthUserInfoDto socialAuthUserInfoDto = socialAuthContext
            .getAuthUserInfo(request.getLoginType(), request.getAccessToken());
        Long userId = userService
            .signInOrSignUp(socialAuthUserInfoDto, request.getTermsAgreement());

        return userId;
    }
}
