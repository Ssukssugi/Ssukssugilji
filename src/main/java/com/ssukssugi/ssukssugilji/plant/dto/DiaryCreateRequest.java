package com.ssukssugi.ssukssugilji.plant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssukssugi.ssukssugilji.plant.entity.CareType;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class DiaryCreateRequest {

    private Long plantId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private List<CareType> careTypes;
    private String diary;

    public void setDiary(String diary) {
        this.diary = diary.replace("\n", "\\n");
    }
}
