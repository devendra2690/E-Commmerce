package com.online.buy.order.processor.mapper;

import com.online.buy.order.processor.dto.OrderItemDto;
import com.online.buy.order.processor.model.OrderItemModel;

public class OrderItemMapper {

    public static OrderItemModel dtoToMapper(OrderItemDto orderItemDto, OrderItemModel orderItemModel) {
        orderItemModel.setClientId(orderItemDto.getClientId());
        orderItemModel.setQuantity(orderItemDto.getQuantity());
        orderItemModel.setProductId(orderItemDto.getProductId());
        orderItemModel.setPrice(orderItemDto.getPrice());
        return orderItemModel;
    }

}
