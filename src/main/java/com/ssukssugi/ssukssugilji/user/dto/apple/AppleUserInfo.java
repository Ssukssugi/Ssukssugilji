package com.ssukssugi.ssukssugilji.user.dto.apple;

import lombok.Data;

@Data
public class AppleUserInfo {

    private String localId;
    private String email;
    private Boolean emailVerified;
}
