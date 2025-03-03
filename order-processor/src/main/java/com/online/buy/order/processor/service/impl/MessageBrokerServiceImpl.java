package com.online.buy.order.processor.service.impl;

import com.online.buy.common.code.service.EncryptionServiceImpl;
import com.online.buy.common.code.service.HmacServiceImpl;
import com.online.buy.order.processor.dto.PaymentMessageDto;
import com.online.buy.order.processor.service.MessageBrokerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MessageBrokerServiceImpl implements MessageBrokerService {

    private final RabbitTemplate rabbitTemplate;
    private final EncryptionServiceImpl encryptionService;
    private final HmacServiceImpl hmacService;

    @Override
    public void sendPaymentRequest(String customerId, Long amount) throws Exception {
        //String encryptedCustomerId = encryptionService.encrypt(customerId);
        String signature = hmacService.sign(customerId + amount);

        PaymentMessageDto message = new PaymentMessageDto(customerId, amount, signature);
        rabbitTemplate.convertAndSend("order.exchange", "payment.process", message);
    }
}
