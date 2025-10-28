package com.ssukssugi.ssukssugilji.plant.service;

import com.ssukssugi.ssukssugilji.plant.dao.GrowthReportRepository;
import com.ssukssugi.ssukssugilji.plant.entity.Growth;
import com.ssukssugi.ssukssugilji.plant.entity.GrowthReport;
import com.ssukssugi.ssukssugilji.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GrowthReportService {

    private final GrowthReportRepository growthReportRepository;

    @Transactional
    public void createReport(User user, Growth growth) {
        growthReportRepository.save(
            GrowthReport.builder()
                .growth(growth)
                .reporter(user)
                .build()
        );
    }
}
