package com.online.buy.notification.processor.listner;

import com.online.buy.notification.processor.dto.MessageDto;
import com.online.buy.notification.processor.service.OrderNotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class MessageListner {

    private final OrderNotificationService orderNotificationService;

    @RabbitListener(queues = "notification.processor")
    public void notificationListner(MessageDto messageDto) {
        orderNotificationService.sendOrderNotification(messageDto);
    }
}
