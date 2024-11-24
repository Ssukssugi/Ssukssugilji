package com.ssukssugi.ssukssugilji.user.dto.naver;

import com.ssukssugi.ssukssugilji.user.dto.SocialUserInfoResponse;
import lombok.Getter;

@Getter
public class NaverUserInfoResponse extends SocialUserInfoResponse {
    // https://developers.naver.com/docs/login/profile/profile.md

    private String resultCode;
    private String message;
    private NaverResponse response;
}
