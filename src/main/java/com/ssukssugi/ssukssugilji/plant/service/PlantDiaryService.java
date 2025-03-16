package com.ssukssugi.ssukssugilji.plant.service;

import com.ssukssugi.ssukssugilji.common.CloudflareR2Service;
import com.ssukssugi.ssukssugilji.plant.dao.PlantDiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlantDiaryService {

    private final PlantDiaryRepository plantDiaryRepository;
    private final CloudflareR2Service cloudflareR2Service;
}
