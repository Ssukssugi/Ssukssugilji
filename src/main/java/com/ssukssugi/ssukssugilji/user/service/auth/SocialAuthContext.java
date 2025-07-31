package com.ssukssugi.ssukssugilji.user.service.auth;

import com.ssukssugi.ssukssugilji.user.dto.LoginType;
import com.ssukssugi.ssukssugilji.user.dto.SocialAuthUserInfoDto;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocialAuthContext {

    private final List<SocialAuthService> socialAuthServices;
    private Map<LoginType, SocialAuthService> socialAuthServiceMap;

    @PostConstruct
    void init() {
        socialAuthServiceMap = socialAuthServices.stream()
            .collect(Collectors.toMap(SocialAuthService::getLoginType, Function.identity()));
    }

    public SocialAuthUserInfoDto getAuthUserInfo(LoginType loginType, String accessToken) {
        log.info("Fetching user info for login type: {}, accessToken: {}", loginType, accessToken);
        SocialAuthService socialAuthService = socialAuthServiceMap.get(loginType);
        if (socialAuthService == null) {
            throw new IllegalArgumentException("Invalid login type");
        }
        return socialAuthService.getAuthUserInfo(accessToken);
    }
}
