package com.ssukssugi.ssukssugilji.common.error;

public enum ErrorCode {
    INTERNAL_SERVER_ERROR("50000"),
    INVALID_REQUEST("40000"),
    ;

    private String code;

    ErrorCode(String code) {
        this.code = code;
    }
}
