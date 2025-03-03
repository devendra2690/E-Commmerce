package com.online.buy.order.processor.scheduler;

import com.online.buy.order.processor.repository.BatchUpdateRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ReservationItemCheckScheduler {

    private final BatchUpdateRepository batchUpdateRepository;

    @Scheduled(cron = "${reservation.check.scheduler.cron}") // Runs every 20 seconds
    @SchedulerLock(name = "uniqueTaskName",lockAtMostFor = "5m",lockAtLeastFor = "1m")// Executes every 1m
    public void processOrders() {

        long startTime = System.currentTimeMillis();
        log.info("START : Running scheduled job to reverse reservation made for Order Item if payment not done in 30 seconds job : Job started at {}", startTime);

        batchUpdateRepository.batchUpdateReservations();

        log.info("END : Job took :{} milliseconds",(System.currentTimeMillis()-startTime));
    }
}
