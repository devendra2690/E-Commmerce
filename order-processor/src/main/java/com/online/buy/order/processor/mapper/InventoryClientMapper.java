package com.online.buy.order.processor.mapper;

import com.online.buy.common.code.dto.inventory.InventoryItemClientDto;
import com.online.buy.order.processor.model.OrderItemModel;

public class InventoryClientMapper {

    public static InventoryItemClientDto orderItemModelToInventoryRequest(OrderItemModel orderItemModel, InventoryItemClientDto inventoryItemClientDto) {
        inventoryItemClientDto.setProductId(orderItemModel.getProductId());
        inventoryItemClientDto.setQuantity(orderItemModel.getQuantity());
        return inventoryItemClientDto;
    }
}
