package com.ssukssugi.ssukssugilji.user.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class PlantReasonListConverter implements AttributeConverter<List<PlantReason>, String> {

    @Override
    public String convertToDatabaseColumn(List<PlantReason> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.stream()
            .map(PlantReason::name)
            .collect(Collectors.joining(","));
    }

    @Override
    public List<PlantReason> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(dbData.split(","))
            .map(PlantReason::valueOf)
            .collect(Collectors.toList());
    }
}
