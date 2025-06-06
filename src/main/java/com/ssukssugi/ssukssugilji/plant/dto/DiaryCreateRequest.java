package com.ssukssugi.ssukssugilji.plant.dto;

import com.ssukssugi.ssukssugilji.plant.entity.CareType;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DiaryCreateRequest {

    private Long plantId;
    private LocalDate date;
    private CareType careType;
    private String diary;
    private MultipartFile plantImage;
}
