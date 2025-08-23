package com.ssukssugi.ssukssugilji.user.dto.apple;

import com.ssukssugi.ssukssugilji.user.dto.SocialUserInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AppleUserInfoResponse extends SocialUserInfoResponse {

    private String accessToken;
    private String tokenType;
    private Integer expiresIn;
    private String refreshToken;
    private String idToken;
}
