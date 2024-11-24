package com.ssukssugi.ssukssugilji.user.dto.kakao;

import lombok.Data;

@Data
public class KakaoAccount {

    // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#kakaoaccount
    private String email;
    private Boolean is_email_valid;
    private Boolean is_email_verified;
}
