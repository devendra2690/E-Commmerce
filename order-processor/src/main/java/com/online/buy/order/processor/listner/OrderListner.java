package com.online.buy.order.processor.listner;

import com.online.buy.order.processor.dto.OrderMessageDto;
import com.online.buy.order.processor.enums.OrderStatus;
import com.online.buy.order.processor.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RabbitListener
@Component
@AllArgsConstructor
public class OrderListner {

    private final RabbitTemplate rabbitTemplate;
    private final OrderService orderService;

    @RabbitListener(queues = "order.queue")
    public void orderProcessListner(OrderMessageDto orderMessageDto) {

        log.info("Received Message to finalize order Order Id:: {} Status {}", orderMessageDto.getOrderId(),orderMessageDto.getStatus());
        orderService.updateOrder(orderMessageDto.getOrderId(), orderMessageDto.getStatus());
    }
}
