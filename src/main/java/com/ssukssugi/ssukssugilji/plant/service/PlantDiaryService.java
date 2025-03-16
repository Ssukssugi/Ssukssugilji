package com.ssukssugi.ssukssugilji.plant.service;

import com.ssukssugi.ssukssugilji.common.CloudflareR2Service;
import com.ssukssugi.ssukssugilji.plant.dao.PlantDiaryRepository;
import com.ssukssugi.ssukssugilji.plant.dto.PlantDiaryCreateRequest;
import com.ssukssugi.ssukssugilji.plant.entity.PlantDiary;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlantDiaryService {

    private final PlantDiaryRepository plantDiaryRepository;
    private final CloudflareR2Service cloudflareR2Service;

    private static final String dir = "plant_diary/";

    @Transactional
    public void createPlantDiary(PlantDiaryCreateRequest request) {
        String imageUrl = dir
            + request.getPlantId()
            + request.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
            + "_" + UUID.randomUUID();
        plantDiaryRepository.save(
            PlantDiary.builder()
                .date(request.getDate())
                .imageUrl(imageUrl)
                .build()
        );

        try {
            cloudflareR2Service.uploadFile(imageUrl, request.getImage());
        } catch (Exception e) {
            throw new RuntimeException("Cloudflare R2 service exception", e);
        }
    }
}
