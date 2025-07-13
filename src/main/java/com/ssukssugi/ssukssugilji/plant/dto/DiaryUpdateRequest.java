package com.ssukssugi.ssukssugilji.plant.dto;

import com.ssukssugi.ssukssugilji.plant.entity.CareType;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class DiaryUpdateRequest {

    private LocalDate date;
    private List<CareType> careTypes;
    private String diary;
    private Boolean updateImage;
}
