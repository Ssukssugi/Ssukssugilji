package com.ssukssugi.ssukssugilji.auth.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum JwtTokenStatus {
    AUTHENTICATED,
    EXPIRED,
    INVALID
}