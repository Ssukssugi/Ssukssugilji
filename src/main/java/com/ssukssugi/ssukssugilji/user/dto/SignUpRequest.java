package com.ssukssugi.ssukssugilji.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotNull
    private String accessToken;
    @NotNull
    private LoginType loginType;
    private TermsAgreement termsAgreement;
}
