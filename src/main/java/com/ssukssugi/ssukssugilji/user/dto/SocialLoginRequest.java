package com.ssukssugi.ssukssugilji.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginRequest {

    private String accessToken;
    private LoginType loginType;
    private TermsAgreement termsAgreement;
}
