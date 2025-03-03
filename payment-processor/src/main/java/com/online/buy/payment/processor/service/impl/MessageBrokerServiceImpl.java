package com.online.buy.payment.processor.service.impl;

import com.online.buy.payment.processor.dto.OrderMessageDto;
import com.online.buy.payment.processor.dto.ReservationMessageDto;
import com.online.buy.payment.processor.service.MessageBrokerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MessageBrokerServiceImpl implements MessageBrokerService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendOrderProcessMessage(Long orderId, List<Long> reservationId, String status) {

        try {
            log.info("Processing message to Order queue to mark Order {} As {}", orderId, status);
            OrderMessageDto orderMessageDto = new OrderMessageDto(orderId, status);
            rabbitTemplate.convertAndSend("order.exchange", "payment.process", orderMessageDto);
            log.info("Message Sent to Order queue");


            log.info("Processing message to complete reservation of Product Inventory for Order {}", orderId);
            ReservationMessageDto reservationMessageDto = new ReservationMessageDto(reservationId);
            rabbitTemplate.convertAndSend("order.exchange", "reservation.process", reservationMessageDto);
            log.info("Message Sent to Order queue");
        } catch (Exception exception) {
            log.error("Error while sending message " + exception.getMessage());
            throw new HttpServerErrorException(HttpStatusCode.valueOf(500), "Error while sending message to Order Queue");
        }
    }
}
