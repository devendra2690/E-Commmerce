package com.online.buy.payment.processor.listner;

import com.online.buy.common.code.service.EncryptionServiceImpl;
import com.online.buy.common.code.service.HmacServiceImpl;
import com.online.buy.payment.processor.dto.PaymentMessageDto;
import com.online.buy.payment.processor.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentListener {

    private final EncryptionServiceImpl encryptionService;
    private final HmacServiceImpl hmacService;
    private final PaymentService paymentService;

    @RabbitListener(queues = "payment.queue")
    public void receiveMessage(PaymentMessageDto message) {

        try {
            log.info("Received Message: " + message);
            String decryptedCustomerId = encryptionService.decrypt(message.getCustomerId());

            if (!hmacService.verify(message.getCustomerId() + message.getAmount(), message.getSignature())) {
                throw new SecurityException("Invalid payment request: Possible attack detected!");
            }

            paymentService.chargeCustomer(message, decryptedCustomerId);
        }catch (Exception e) {
            log.info("Error processing message: " + message);
            // Send to DLQ instead of re-queuing infinitely
            throw new AmqpRejectAndDontRequeueException("Message failed permanently");
        }
    }
}

