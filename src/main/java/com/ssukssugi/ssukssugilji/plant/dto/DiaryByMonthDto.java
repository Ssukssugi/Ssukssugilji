package com.ssukssugi.ssukssugilji.plant.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiaryByMonthDto {

    private Short year;
    private Short month;
    private List<DiaryDto> diaries;
}
