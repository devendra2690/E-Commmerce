package com.online.buy.order.processor.service.impl;

import com.online.buy.common.code.service.EncryptionServiceImpl;
import com.online.buy.common.code.service.HmacServiceImpl;
import com.online.buy.order.processor.dto.PaymentMessageDto;
import com.online.buy.order.processor.service.MessageBrokerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MessageBrokerServiceImpl implements MessageBrokerService {

    private final RabbitTemplate rabbitTemplate;
    private final EncryptionServiceImpl encryptionService;
    private final HmacServiceImpl hmacService;

    @Override
    public void sendPaymentRequest(String customerId, Long amount, Long orderId, List<Long> reservationId) throws Exception {

        try {
            //String encryptedCustomerId = encryptionService.encrypt(customerId);
            String signature = hmacService.sign(customerId + amount);

            PaymentMessageDto message = new PaymentMessageDto(customerId, amount, signature,orderId, reservationId);
            rabbitTemplate.convertAndSend("order.exchange", "payment.process", message);
        }catch (Exception exception) {
            log.error("Error while sending message "+exception.getMessage());
            throw new HttpServerErrorException(HttpStatusCode.valueOf(500), "Error while sending message to payment queue to process payment");
        }
    }
}
