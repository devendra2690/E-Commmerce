package com.online.buy.payment.processor.listner;

import com.online.buy.common.code.service.EncryptionServiceImpl;
import com.online.buy.common.code.service.HmacServiceImpl;
import com.online.buy.payment.processor.dto.PaymentMessageDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentListener {

    private final EncryptionServiceImpl encryptionService;
    private final HmacServiceImpl hmacService;

    @RabbitListener(queues = "payment.queue")
    public void receiveMessage(PaymentMessageDto message) throws Exception {
        log.info("Received Message: " + message);
        String decryptedCustomerId = encryptionService.decrypt(message.getCustomerId());

        if (!hmacService.verify(message.getCustomerId() + message.getAmount(), message.getSignature())) {
            throw new SecurityException("Invalid payment request: Possible attack detected!");
        }

        paymentService.chargeCustomer(decryptedCustomerId, message.getAmount());
    }
}

