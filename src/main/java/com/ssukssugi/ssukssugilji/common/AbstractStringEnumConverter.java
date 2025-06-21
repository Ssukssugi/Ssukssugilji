package com.ssukssugi.ssukssugilji.common;

import jakarta.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractStringEnumConverter<T extends Enum<T>> implements
    AttributeConverter<List<T>, String> {

    private final Class<T> enumClass;

    @Override
    public String convertToDatabaseColumn(List<T> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return attribute.stream()
            .map(Enum::name)
            .collect(Collectors.joining(","));
    }

    @Override
    public List<T> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(dbData.split(","))
            .map(str -> T.valueOf(enumClass, str))
            .collect(Collectors.toList());
    }
}
