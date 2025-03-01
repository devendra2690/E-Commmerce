package com.online.buy.order.processor.scheduler;

import com.online.buy.order.processor.repository.BatchUpdateRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ReservationItemCheckScheduler {

    private final BatchUpdateRepository batchUpdateRepository;

    @Scheduled(cron = "${reservation.check.scheduler.cron}") // Runs every 20 seconds
    public void processOrders() {

        long startTime = System.currentTimeMillis();
        log.info("Running scheduled order processing job at {}", startTime);

        batchUpdateRepository.batchUpdateReservations();

        log.info("Job took :{} milliseconds",(System.currentTimeMillis()-startTime));
    }
}
