package com.ssukssugi.ssukssugilji.user.entity;

import com.ssukssugi.ssukssugilji.common.AbstractStringEnumConverter;
import jakarta.persistence.Converter;

@Converter
public class PlantReasonListConverter extends AbstractStringEnumConverter<PlantReason> {

    public PlantReasonListConverter() {
        super(PlantReason.class);
    }
}
