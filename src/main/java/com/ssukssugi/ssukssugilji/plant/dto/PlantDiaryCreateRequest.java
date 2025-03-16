package com.ssukssugi.ssukssugilji.plant.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PlantDiaryCreateRequest {

    private Long plantId;
    private String plantCategory;
    private MultipartFile image;
}
