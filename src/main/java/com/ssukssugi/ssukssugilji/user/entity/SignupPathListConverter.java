package com.ssukssugi.ssukssugilji.user.entity;

import com.ssukssugi.ssukssugilji.common.AbstractStringEnumConverter;
import jakarta.persistence.Converter;

@Converter
public class SignupPathListConverter extends AbstractStringEnumConverter<SignUpPath> {

    public SignupPathListConverter() {
        super(SignUpPath.class);
    }
}
