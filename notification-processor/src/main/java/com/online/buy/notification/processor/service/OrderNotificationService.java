package com.online.buy.notification.processor.service;

import com.online.buy.notification.processor.dto.MessageDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OrderNotificationService {

    private final EmailService emailService;

    public void sendOrderNotification(MessageDto messageDto) {

       try {

            // TODO: Get Order Details and User details from input object, use template to make body then call email service

            String body = ""; // prepare body of email
            emailService.sendEmail(messageDto.getEmail(), "Order Status for ID:" + messageDto.getOrderDto().getOrderId() + " is " + messageDto.getStatus(), body);
        }catch (Exception exception) {
            log.error("Error occurred while sending order details to consumer {} for Order {}. Exception occurred : {}", messageDto.getUserId(), messageDto.getOrderDto().getOrderId(), exception.getMessage());
             // TODO: Send message DTO to DLQ for futher investigation in case issue i caused by Message to investigate further.
        }
    }

}
