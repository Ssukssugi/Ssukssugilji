package com.ssukssugi.ssukssugilji.user.dto.google;

import lombok.Data;

@Data
public class GoogleUserInfo {

    private String localId;
    private String email;
    private Boolean emailVerified;
}
