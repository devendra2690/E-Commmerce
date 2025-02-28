package com.online.buy.inventory.processor.mapper;

import com.online.buy.common.code.dto.inventory.InventoryItemClientDto;
import com.online.buy.common.code.dto.inventory.InventoryClientDto;
import com.online.buy.inventory.processor.model.OrderItemModel;
import com.online.buy.inventory.processor.model.OrderModel;

public class InventoryMapper {

    public static OrderModel dtoToModel(InventoryClientDto inventoryClientDto, OrderModel orderModel) {

        orderModel.setUserId(inventoryClientDto.getUserId());
        orderModel.setOrderItemModelList(inventoryClientDto.getInventoryItemClientDtos().stream()
                .map(inventoryItemRequestDto -> InventoryMapper.itemDtoToModel(inventoryItemRequestDto, new OrderItemModel())).toList());
        return orderModel;
    }

    public static InventoryClientDto modelToDto(OrderModel orderModel, InventoryClientDto inventoryClientDto) {

        inventoryClientDto.setUserId(orderModel.getUserId());
        inventoryClientDto.setInventoryItemClientDtos(orderModel.getOrderItemModelList().stream()
                .map(orderItemModel -> InventoryMapper.itemModelToDto(orderItemModel, new InventoryItemClientDto())).toList());
        return inventoryClientDto;
    }

    public static InventoryItemClientDto itemModelToDto(OrderItemModel orderItemModel, InventoryItemClientDto inventoryItemClientDto) {
        inventoryItemClientDto.setProductId(orderItemModel.getProductId());
        inventoryItemClientDto.setQuantity(orderItemModel.getQuantity());
        inventoryItemClientDto.setSuccess(orderItemModel.isSuccess());
        inventoryItemClientDto.setMessage(orderItemModel.getMessage());
        inventoryItemClientDto.setReservationId(orderItemModel.getReservationId());
        return inventoryItemClientDto;
    }

    public static OrderItemModel itemDtoToModel(InventoryItemClientDto inventoryItemClientDto, OrderItemModel orderItemModel) {

        orderItemModel.setProductId(inventoryItemClientDto.getProductId());
        orderItemModel.setQuantity(inventoryItemClientDto.getQuantity());
        return orderItemModel;
    }
}
