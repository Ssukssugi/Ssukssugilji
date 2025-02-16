package com.ssukssugi.ssukssugilji.user.service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ssukssugi.ssukssugilji.common.webclient.AppleWebClientProxyImpl;
import com.ssukssugi.ssukssugilji.user.dto.LoginType;
import com.ssukssugi.ssukssugilji.user.dto.SocialAuthUserInfoDto;
import com.ssukssugi.ssukssugilji.user.dto.apple.AppleUserInfoResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Getter
public class AppleAuthService implements SocialAuthService {

    private final AppleWebClientProxyImpl appleWebClientProxy;
    private static final LoginType LOGIN_TYPE = LoginType.APPLE;

    @Override
    public LoginType getLoginType() {
        return LOGIN_TYPE;
    }

    @Override
    public SocialAuthUserInfoDto getAuthUserInfo(String accessToken) {
        AppleUserInfoResponse response = appleWebClientProxy.getUserInfo(accessToken);
        DecodedJWT jwt = JWT.decode(response.getIdToken());

        return SocialAuthUserInfoDto.builder()
            .socialId(jwt.getSubject())
            .emailAddress(jwt.getClaim("email").asString())
            .loginType(LOGIN_TYPE)
            .build();
    }
}
