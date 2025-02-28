package com.online.buy.order.processor.mapper;

import com.online.buy.order.processor.dto.OrderItemDto;
import com.online.buy.order.processor.entity.OrderItem;
import com.online.buy.order.processor.model.OrderItemModel;
import com.online.buy.order.processor.model.OrderModel;

import java.math.BigDecimal;

public class OrderItemMapper {

    public static OrderItemModel dtoToMapper(OrderItemDto orderItemDto, OrderItemModel orderItemModel) {
        orderItemModel.setClientId(orderItemDto.getClientId());
        orderItemModel.setQuantity(orderItemDto.getQuantity());
        orderItemModel.setProductId(orderItemDto.getProductId());
        orderItemModel.setPrice(orderItemDto.getPrice());
        return orderItemModel;
    }

    public static OrderItemModel itemModelToEntity(OrderItemModel orderItemModel, OrderItem orderItem) {
        orderItem.setQuantity(orderItemModel.getQuantity());
        orderItem.setPrice(BigDecimal.valueOf(orderItemModel.getPrice()));
        return orderItemModel;
    }

    public static OrderItemModel entityToModel(OrderItem orderItem, OrderItemModel orderItemModel) {
        orderItemModel.setQuantity(orderItem.getQuantity());
        orderItemModel.setPrice(orderItem.getPrice().doubleValue());
        orderItemModel.setClientId(orderItem.getClient().getId());
        orderItemModel.setProductId(orderItem.getProduct().getId());
        orderItemModel.setId(orderItem.getId());
        return orderItemModel;
    }

    public static OrderItemDto modelToDto(OrderItemModel orderItemModel, OrderItemDto orderItemDto) {
        orderItemDto.setQuantity(orderItemModel.getQuantity());
        orderItemDto.setPrice(orderItemModel.getPrice());
        orderItemDto.setClientId(orderItemModel.getClientId());
        orderItemDto.setProductId(orderItemModel.getProductId());
        orderItemDto.setId(orderItemModel.getId());
        return orderItemDto;
    }

}
