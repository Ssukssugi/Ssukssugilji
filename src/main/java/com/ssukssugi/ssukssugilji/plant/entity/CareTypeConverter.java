package com.ssukssugi.ssukssugilji.plant.entity;

import com.ssukssugi.ssukssugilji.common.AbstractStringEnumConverter;
import jakarta.persistence.Converter;

@Converter
public class CareTypeConverter extends AbstractStringEnumConverter<CareType> {

    public CareTypeConverter() {
        super(CareType.class);
    }
}