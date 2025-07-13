package com.ssukssugi.ssukssugilji.plant.service;

import com.ssukssugi.ssukssugilji.common.CloudflareR2Service;
import com.ssukssugi.ssukssugilji.plant.dao.DiaryRepository;
import com.ssukssugi.ssukssugilji.plant.dto.DiaryByMonthDto;
import com.ssukssugi.ssukssugilji.plant.dto.DiaryByMonthListDto;
import com.ssukssugi.ssukssugilji.plant.dto.DiaryCreateRequest;
import com.ssukssugi.ssukssugilji.plant.dto.DiaryDto;
import com.ssukssugi.ssukssugilji.plant.entity.Diary;
import com.ssukssugi.ssukssugilji.plant.entity.Plant;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final CloudflareR2Service cloudflareR2Service;

    private static final String dir = "plant_diary/";

    @Transactional
    public void createDiary(DiaryCreateRequest request, Plant plant) {
        String imageUrl = buildImageUrl(request);

        Diary diary = Diary.builder()
            .date(request.getDate())
            .careTypes(request.getCareTypes())
            .diary(request.getDiary())
            .imageUrl(imageUrl)
            .build();
        plant.addDiary(diary);
        Diary entity = diaryRepository.save(diary);

        try {
            cloudflareR2Service.uploadFile(imageUrl, request.getPlantImage());
        } catch (Exception e) {
            throw new RuntimeException(
                "Failed to upload file in Cloudflare R2 service, diaryId: " + entity.getDiaryId(),
                e);
        }
    }

    private static String buildImageUrl(DiaryCreateRequest request) {
        return dir
            + request.getPlantId()
            + request.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
            + "_" + UUID.randomUUID();
    }

    public Optional<Diary> getMostRecent(Plant plant) {
        return diaryRepository.findTopByPlantOrderByCreatedAtDesc(plant);
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
                            .sorted(Comparator.comparing(Diary::getDate).reversed())
                            .collect(Collectors.toList())))
                    .build())
                .collect(Collectors.toList())
        );
        return diaryByMonthListDto;
    }

    public void deleteDiary(Long diaryId) {
        diaryRepository.deleteById(diaryId);
    }
}
