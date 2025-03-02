package com.online.buy.order.processor.mapper;

import com.online.buy.order.processor.dto.OrderItemDto;
import com.online.buy.order.processor.dto.PaymentMessageDto;
import com.online.buy.order.processor.entity.Order;

public class PaymentDetailsMapper {

    public static PaymentMessageDto mapOrderDtoToPaymentDto(Order order, PaymentMessageDto paymentMessageDto) {

        paymentMessageDto.setOrderId(order.getId());
        paymentMessageDto.setPaymentMode(order.getPaymentMode().toString());
        paymentMessageDto.setEmail(order.getEmail());
        paymentMessageDto.setUserId(order.getUser().getId().toString());

        paymentMessageDto.setItems(order.getOrderItems().stream().map(orderItem -> {

            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setId(orderItem.getId());
            orderItemDto.setPrice(orderItem.getPrice().doubleValue());
            orderItemDto.setClientId(orderItem.getClient().getId());
            orderItemDto.setProductId(orderItem.getProduct().getId());
            orderItemDto.setQuantity(orderItem.getQuantity());

            return orderItemDto;
        }).toList());

        return paymentMessageDto;
    }
}
