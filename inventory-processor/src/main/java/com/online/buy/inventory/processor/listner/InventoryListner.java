package com.online.buy.inventory.processor.listner;

import com.online.buy.inventory.processor.dto.ReservationMessageDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class InventoryListner {

    @RabbitListener(queues = "reservation.queue")
    public void inventoryUpdate(ReservationMessageDto reservationMessageDto) {

    }
}
