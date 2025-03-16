package com.ssukssugi.ssukssugilji.plant.dto;

import java.time.LocalDate;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PlantDiaryCreateRequest {

    private Long plantId;
    private LocalDate date;
    private String plantCategory;
    private MultipartFile image;
}
