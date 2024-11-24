package com.ssukssugi.ssukssugilji.common.webclient;


import com.ssukssugi.ssukssugilji.user.dto.SocialUserInfoResponse;

public interface WebClientProxy {

    SocialUserInfoResponse getUserInfo(String accessToken);
}
