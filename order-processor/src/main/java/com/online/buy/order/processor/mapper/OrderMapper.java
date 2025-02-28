package com.online.buy.order.processor.mapper;

import com.online.buy.order.processor.dto.OrderDto;
import com.online.buy.order.processor.dto.OrderItemDto;
import com.online.buy.order.processor.dto.ShippingDetailsDto;
import com.online.buy.order.processor.entity.Order;
import com.online.buy.order.processor.model.OrderItemModel;
import com.online.buy.order.processor.model.OrderModel;
import com.online.buy.order.processor.model.ShippingDetailsModel;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static OrderModel dtpToModel(OrderDto orderDto, OrderModel orderModel) {

        orderModel.setEmail(orderDto.getEmail());
        orderModel.setUserId(orderDto.getUserId());

        List <OrderItemModel> orderItemModels = orderDto.getItems().stream().map(orderItemDto ->
                OrderItemMapper.dtoToMapper(orderItemDto, new OrderItemModel())).toList();
        orderModel.setItems(orderItemModels);

        orderModel.setShippingDetails(ShippingAddressMapper.dtoToMapper(orderDto.getShippingDetails(), new ShippingDetailsModel()));
        orderModel.setPaymentMode(orderDto.getPaymentMode());

        return orderModel;
    }

    public static OrderModel entityToModel(Order order, OrderModel orderModel) {

        orderModel.setOrderId(order.getId());
        orderModel.setEmail(order.getEmail());
        orderModel.setUserId(order.getUser().getId().toString());

        if(!CollectionUtils.isEmpty(order.getOrderItems())) {
            List <OrderItemModel> orderItemModels = order.getOrderItems().stream().map(orderItem ->
                    OrderItemMapper.entityToModel(orderItem, new OrderItemModel())).toList();
            orderModel.setItems(orderItemModels);
        }

        orderModel.setShippingDetails(ShippingAddressMapper.entityToModel(order.getAddressEntity(), new ShippingDetailsModel()));
        orderModel.setPaymentMode(order.getPaymentMode());

        return orderModel;
    }

    public static OrderDto modelToDto(OrderModel orderModel, OrderDto orderDto) {

        orderDto.setOrderId(orderModel.getOrderId());
        orderDto.setEmail(orderModel.getEmail());
        orderDto.setUserId(orderModel.getUserId());

        List <OrderItemDto> orderItemModels = orderModel.getItems().stream().map(orderItem ->
                OrderItemMapper.modelToDto(orderItem, new OrderItemDto())).toList();
        orderDto.setItems(orderItemModels);

        orderDto.setShippingDetails(ShippingAddressMapper.modelToDto(orderModel.getShippingDetails(), new ShippingDetailsDto()));
        orderDto.setPaymentMode(orderModel.getPaymentMode());
        orderDto.setAdditionalInfo(orderModel.getAdditionalInfo());
        return orderDto;
    }

}
