package com.ssukssugi.ssukssugilji.user.service.auth;


import com.ssukssugi.ssukssugilji.user.dto.LoginType;
import com.ssukssugi.ssukssugilji.user.dto.SocialAuthUserInfoDto;

public interface SocialAuthService {

    SocialAuthUserInfoDto getAuthUserInfo(String accessToken);

    LoginType getLoginType();
}
