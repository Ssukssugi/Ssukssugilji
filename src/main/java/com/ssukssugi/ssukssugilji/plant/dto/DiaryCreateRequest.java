package com.ssukssugi.ssukssugilji.plant.dto;

import com.ssukssugi.ssukssugilji.plant.entity.CareType;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DiaryCreateRequest {

    private Long plantId;
    private LocalDate date;
    private List<CareType> careTypes;
    private String diary;
    private MultipartFile plantImage;
}
