package com.online.buy.notification.processor.scheduler;

import com.online.buy.common.code.repository.UserRepository;
import com.online.buy.notification.processor.dto.UnverifiedUserModel;
import com.online.buy.notification.processor.service.EmailService;
import com.online.buy.notification.processor.service.UserService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class UserVerificationMailScheduler {

    private final UserService userService;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 1 * * *")
    @SchedulerLock(name = "userVerification", lockAtMostFor = "5m", lockAtLeastFor = "2m")
    public void sendUnverfifiedEmailARemainder() {


        long startTime = System.currentTimeMillis();
        log.info("START : Running scheduled job to reverse reservation made for Order Item if payment not done in 30 seconds job : Job started at {}", startTime);
        try{

            List<UnverifiedUserModel> unverifiedUserModels = userService.fetchUnVerifiedList();
            // Call Email service to send email verification
            for (UnverifiedUserModel unverifiedUserModel : unverifiedUserModels) {
                emailService.sendEmail(unverifiedUserModel.getEmail(), unverifiedUserModel.getUsername(), "");
            }
        }catch (Exception e) {
            log.error("Error Occurred while Sending remainder to verify email Exception: {} ",e.getMessage());
            // TODO: Send notification via email to inform about issue
        }
        log.info("END : Job took :{} milliseconds",(System.currentTimeMillis()-startTime));
    }
}
