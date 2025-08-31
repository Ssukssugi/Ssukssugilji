package com.ssukssugi.ssukssugilji.user.dto.google;

import com.ssukssugi.ssukssugilji.user.dto.SocialUserInfoResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GoogleUserInfoResponse extends SocialUserInfoResponse {

    private String localId;
    private String email;
    private Boolean emailVerified;
}
