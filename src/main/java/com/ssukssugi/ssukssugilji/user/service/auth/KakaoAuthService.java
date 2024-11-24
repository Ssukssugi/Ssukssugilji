package com.ssukssugi.ssukssugilji.user.service.auth;

import com.ssukssugi.ssukssugilji.common.webclient.KakaoWebClientProxyImpl;
import com.ssukssugi.ssukssugilji.user.dto.LoginType;
import com.ssukssugi.ssukssugilji.user.dto.SocialAuthUserInfoDto;
import com.ssukssugi.ssukssugilji.user.dto.kakao.KakaoUserInfoResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Getter
public class KakaoAuthService implements SocialAuthService {

    private final KakaoWebClientProxyImpl kakaoWebClientProxy;
    private static final LoginType LOGIN_TYPE = LoginType.KAKAO;

    @Override
    public LoginType getLoginType() {
        return LOGIN_TYPE;
    }

    @Override
    public SocialAuthUserInfoDto getAuthUserInfo(String accessToken) {
        KakaoUserInfoResponse userInfo = kakaoWebClientProxy.getUserInfo(accessToken);
        validateResponse(userInfo);

        return SocialAuthUserInfoDto.builder()
            .socialId(String.valueOf(userInfo.getId()))
            .emailAddress(userInfo.getKakaoAccount().getEmail())
            .loginType(LOGIN_TYPE)
            .build();
    }

    private void validateResponse(KakaoUserInfoResponse response) {
        if (!response.getKakaoAccount().getIs_email_valid()
            || !response.getKakaoAccount().getIs_email_verified()) {
            throw new RuntimeException("Kakao account email is not valid or verified");
        }
    }
}
