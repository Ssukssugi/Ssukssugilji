package com.ssukssugi.ssukssugilji.plant.service;

import com.ssukssugi.ssukssugilji.common.CloudflareR2Service;
import com.ssukssugi.ssukssugilji.plant.dao.DiaryRepository;
import com.ssukssugi.ssukssugilji.plant.dto.DiaryCreateRequest;
import com.ssukssugi.ssukssugilji.plant.entity.Diary;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final CloudflareR2Service cloudflareR2Service;

    private static final String dir = "plant_diary/";

    @Transactional
    public void createDiary(DiaryCreateRequest request) {
        String imageUrl = dir
            + request.getPlantId()
            + request.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
            + "_" + UUID.randomUUID();
        diaryRepository.save(
            Diary.builder()
                .plantId(request.getPlantId())
                .date(request.getDate())
                .careType(request.getCareType())
                .diary(request.getDiary())
                .imageUrl(imageUrl)
                .build()
        );

        try {
            cloudflareR2Service.uploadFile(imageUrl, request.getPlantImage());
        } catch (Exception e) {
            throw new RuntimeException("Cloudflare R2 service exception: ", e);
        }
    }
}
