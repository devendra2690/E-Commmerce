package com.online.buy.order.processor.service.impl;

import com.online.buy.order.processor.dto.PaymentMessageDto;
import com.online.buy.order.processor.entity.Order;
import com.online.buy.order.processor.mapper.PaymentDetailsMapper;
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

    @Override
    public void processMessageToPaymentService(Order order) {

        log.info("Processing order: {}", order);
        // Send message to PaymentService
        rabbitTemplate.convertAndSend("order.exchange", "payment.process", PaymentDetailsMapper.mapOrderDtoToPaymentDto(order, new PaymentMessageDto()));
        log.info("Messages sent to RabbitMQ for Payment Service.");
    }
}
