package com.ssukssugi.ssukssugilji.plant.dto;

import com.ssukssugi.ssukssugilji.plant.entity.Diary;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record SimpleDiaryVo(
    String imageUrl,
    LocalDate date
) {

    public static SimpleDiaryVo from(Diary beforeDiary) {
        return SimpleDiaryVo.builder()
            .imageUrl(beforeDiary.getImageUrl())
            .date(beforeDiary.getDate())
            .build();
    }
}
