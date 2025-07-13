package com.ssukssugi.ssukssugilji.plant.dto;

import com.ssukssugi.ssukssugilji.plant.entity.CareType;
import com.ssukssugi.ssukssugilji.plant.entity.Diary;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiaryDto {

    private Long diaryId;
    private LocalDate date;
    private String image;
    private String content;
    private List<CareType> cares;

    private static DiaryDto fromEntity(Diary entity) {
        return DiaryDto.builder()
            .diaryId(entity.getDiaryId())
            .date(entity.getDate())
            .image(entity.getImageUrl())
            .content(entity.getDiary())
            .build();
    }

    public static List<DiaryDto> fromEntities(List<Diary> entities) {
        return entities.stream()
            .map(DiaryDto::fromEntity)
            .collect(Collectors.toList());
    }
}
