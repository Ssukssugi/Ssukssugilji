package com.ssukssugi.ssukssugilji.user.dto.kakao;

import com.ssukssugi.ssukssugilji.user.dto.SocialUserInfoResponse;
import lombok.Getter;

@Getter
public class KakaoUserInfoResponse extends SocialUserInfoResponse {
    // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info-response

    private Long id;
    private KakaoAccount kakao_account;
}
