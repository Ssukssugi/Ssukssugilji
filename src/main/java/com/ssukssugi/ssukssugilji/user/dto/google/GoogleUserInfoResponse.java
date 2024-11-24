package com.ssukssugi.ssukssugilji.user.dto.google;

import com.ssukssugi.ssukssugilji.user.dto.SocialUserInfoResponse;
import java.util.List;
import lombok.Getter;

@Getter
public class GoogleUserInfoResponse extends SocialUserInfoResponse {

    private List<GoogleUserInfo> users;
}
