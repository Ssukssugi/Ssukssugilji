package com.ssukssugi.ssukssugilji.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginResponse {

    private Boolean isRegistered;
    private Boolean existInfo;
    private String socialId;
    private String emailAddress;
}
