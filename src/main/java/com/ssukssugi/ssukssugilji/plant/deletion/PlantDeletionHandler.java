package com.ssukssugi.ssukssugilji.plant.deletion;

import com.ssukssugi.ssukssugilji.plant.dto.UserPlantDto;
import com.ssukssugi.ssukssugilji.plant.service.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PlantDeletionHandler {

    private final PlantService plantService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void handle(PlantDeletionEvent event) {
        plantService.getUserPlantList(event.user(), false)
            .stream()
            .map(UserPlantDto::getPlantId)
            .forEach(plantService::deletePlant);
    }
}
