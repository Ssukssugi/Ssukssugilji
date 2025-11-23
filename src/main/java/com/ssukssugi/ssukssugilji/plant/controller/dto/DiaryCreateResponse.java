package com.ssukssugi.ssukssugilji.plant.controller.dto;

import com.ssukssugi.ssukssugilji.common.R2Util;
import com.ssukssugi.ssukssugilji.plant.entity.Diary;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiaryCreateResponse {

    private Long diaryId;
    private String imageUrl;
    private String name;

    public static DiaryCreateResponse fromEntity(Diary entity) {
        return DiaryCreateResponse.builder()
            .diaryId(entity.getDiaryId())
            .imageUrl(R2Util.toR2Url(entity.getImageUrl()))
            .name(entity.getPlant().getName())
            .build();
    }
}
