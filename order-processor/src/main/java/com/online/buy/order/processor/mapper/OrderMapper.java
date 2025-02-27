package com.online.buy.order.processor.mapper;

import com.online.buy.order.processor.dto.OrderDto;
import com.online.buy.order.processor.model.OrderItemModel;
import com.online.buy.order.processor.model.OrderModel;
import com.online.buy.order.processor.model.ShippingDetailsModel;

import java.util.List;

public class OrderMapper {

    public static OrderModel dtpToModel(OrderDto orderDto, OrderModel orderModel) {

        orderModel.setEmail(orderModel.getEmail());
        orderModel.setUserId(orderDto.getUserId());

        List <OrderItemModel> orderItemModels = orderDto.getItems().stream().map(orderItemDto ->
                OrderItemMapper.dtoToMapper(orderItemDto, new OrderItemModel())).toList();
        orderModel.setItems(orderItemModels);

        orderModel.setShippingDetails(ShippingAddressMapper.dtoToMapper(orderDto.getShippingDetails(), new ShippingDetailsModel()));
        orderModel.setPaymentMode(orderDto.getPaymentMode());

        return orderModel;
    }
}
