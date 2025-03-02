package com.online.buy.payment.processor.listner;

import com.online.buy.payment.processor.dto.PaymentMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentListener {

    @RabbitListener(queues = "payment.queue")
    public void receiveMessage(PaymentMessageDto message) {
        log.info("Received Message: " + message);
    }
}

