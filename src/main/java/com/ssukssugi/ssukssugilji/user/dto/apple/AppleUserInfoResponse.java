package com.ssukssugi.ssukssugilji.user.dto.apple;

import com.ssukssugi.ssukssugilji.user.dto.SocialUserInfoResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppleUserInfoResponse extends SocialUserInfoResponse {

    private String accessToken;
    private String tokenType;
    private Integer expiresIn;
    private String refreshToken;
    private String idToken;
}
