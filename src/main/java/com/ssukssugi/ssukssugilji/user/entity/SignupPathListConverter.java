package com.ssukssugi.ssukssugilji.user.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class SignupPathListConverter implements AttributeConverter<List<SignUpPath>, String> {

    @Override
    public String convertToDatabaseColumn(List<SignUpPath> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.stream()
            .map(SignUpPath::name)
            .collect(Collectors.joining(","));
    }

    @Override
    public List<SignUpPath> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(dbData.split(","))
            .map(SignUpPath::valueOf)
            .collect(Collectors.toList());
    }
}
