package com.ssukssugi.ssukssugilji.plant.service;

import com.ssukssugi.ssukssugilji.common.CloudflareR2Service;
import com.ssukssugi.ssukssugilji.plant.controller.dto.DiaryCreateRequest;
import com.ssukssugi.ssukssugilji.plant.controller.dto.DiaryUpdateRequest;
import com.ssukssugi.ssukssugilji.plant.dao.DiaryRepository;
import com.ssukssugi.ssukssugilji.plant.dto.DiaryByMonthDto;
import com.ssukssugi.ssukssugilji.plant.dto.DiaryByMonthListDto;
import com.ssukssugi.ssukssugilji.plant.dto.DiaryDto;
import com.ssukssugi.ssukssugilji.plant.entity.Diary;
import com.ssukssugi.ssukssugilji.plant.entity.Plant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiaryService {

    private static final String PLANT_DIARY_DIR = "/plant_diary/";
    private final DiaryRepository diaryRepository;
    private final CloudflareR2Service cloudflareR2Service;

    private static String buildImageUrl(Long plantId, LocalDate date) {
        return PLANT_DIARY_DIR
            + plantId + "_"
            + date.format(DateTimeFormatter.ISO_LOCAL_DATE) + "_"
            + UUID.randomUUID()
            + ".jpeg";
    }

    public Diary getById(Long diaryId) {
        return diaryRepository.findById(diaryId)
            .orElseThrow(
                () -> new IllegalArgumentException("Diary not found, diaryId = " + diaryId));
    }

    @Transactional
    public Diary createDiary(DiaryCreateRequest request, Plant plant, MultipartFile image) {
        String imageUrl = buildImageUrl(request.getPlantId(), request.getDate());

        Diary diary = Diary.builder()
            .date(request.getDate())
            .careTypes(request.getCareTypes())
            .diary(request.getDiary())
            .imageUrl(imageUrl)
            .build();
        plant.addDiary(diary);
        Diary entity = diaryRepository.save(diary);

        try {
            cloudflareR2Service.uploadFile(imageUrl, image);
        } catch (Exception e) {
            throw new RuntimeException(
                "Failed to upload file in Cloudflare R2 service, diaryId: " + entity.getDiaryId(),
                e);
        }

        return entity;
    }

    @Transactional
    public void updateDiary(
        Plant plant, Long diaryId, DiaryUpdateRequest request, MultipartFile image) {
        Diary diary = getById(diaryId);

        diary.setDate(request.getDate());
        diary.setCareTypes(request.getCareTypes());
        diary.setDiary(request.getDiary());
        if (!diary.getPlant().equals(plant)) {
            diary.setPlant(plant);
        }

        if (request.getUpdateImage()) {
            String imageUrl = buildImageUrl(diary.getPlant().getPlantId(), request.getDate());
            diary.setImageUrl(imageUrl);

            try {
                cloudflareR2Service.uploadFile(imageUrl, image);
            } catch (Exception e) {
                throw new RuntimeException(
                    "Failed to upload file in Cloudflare R2 service, diaryId: "
                        + diary.getDiaryId(),
                    e);
            }
        }
    }

    public Optional<Diary> getMostRecent(Plant plant) {
        return diaryRepository.findTopByPlantOrderByDateDescCreatedAtDesc(plant);
    }

    public DiaryByMonthListDto getDiaryListByMonth(Plant plant) {
        List<Diary> diaries = diaryRepository.findByPlant(plant);
        DiaryByMonthListDto diaryByMonthListDto = new DiaryByMonthListDto();
        diaryByMonthListDto.setByMonth(
            diaries.stream()
                .collect(Collectors.groupingBy(
                    o -> Pair.of(o.getDate().getYear(),
                        o.getDate().getMonth().getValue())))
                .entrySet()
                .stream()
                .sorted((e1, e2) -> {
                    Pair<Integer, Integer> p1 = e1.getKey();
                    Pair<Integer, Integer> p2 = e2.getKey();

                    int yearCompare = p2.getFirst().compareTo(p1.getFirst());
                    if (yearCompare != 0) {
                        return yearCompare;
                    }
                    return p2.getSecond().compareTo(p1.getSecond());
                })
                .map(entry -> DiaryByMonthDto.builder()
                    .year(entry.getKey().getFirst().shortValue())
                    .month(entry.getKey().getSecond().shortValue())
                    .diaries(DiaryDto.fromEntities(
                        entry.getValue().stream()
                            .sorted(Comparator.comparing(Diary::getDate).reversed()
                                .thenComparing(Comparator.comparing(Diary::getCreatedAt).reversed())
                            )
                            .collect(Collectors.toList())))
                    .build())
                .collect(Collectors.toList())
        );
        return diaryByMonthListDto;
    }

    @Transactional
    public void deleteDiary(Long diaryId) {
        diaryRepository.deleteById(diaryId);
    }
}
