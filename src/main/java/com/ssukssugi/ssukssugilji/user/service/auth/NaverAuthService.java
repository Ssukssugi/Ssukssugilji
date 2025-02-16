package com.ssukssugi.ssukssugilji.user.service.auth;

import com.ssukssugi.ssukssugilji.common.webclient.NaverWebClientProxyImpl;
import com.ssukssugi.ssukssugilji.user.dto.LoginType;
import com.ssukssugi.ssukssugilji.user.dto.SocialAuthUserInfoDto;
import com.ssukssugi.ssukssugilji.user.dto.naver.NaverResponse;
import com.ssukssugi.ssukssugilji.user.dto.naver.NaverUserInfoResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Getter
public class NaverAuthService implements SocialAuthService {

    private final NaverWebClientProxyImpl naverWebClientProxy;
    private static final LoginType LOGIN_TYPE = LoginType.NAVER;

    @Override
    public LoginType getLoginType() {
        return LOGIN_TYPE;
    }

    @Override
    public SocialAuthUserInfoDto getAuthUserInfo(String accessToken) {
        NaverUserInfoResponse response = naverWebClientProxy.getUserInfo(accessToken);
        NaverResponse userInfo = response.getResponse();

        return SocialAuthUserInfoDto.builder()
            .socialId(userInfo.getId())
            .emailAddress(userInfo.getEmail())
            .loginType(LOGIN_TYPE)
            .build();
    }
}
