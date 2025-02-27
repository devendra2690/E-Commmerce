package com.online.buy.order.processor.mapper;

import com.online.buy.order.processor.client.dto.InventoryRequest;
import com.online.buy.order.processor.model.OrderItemModel;

public class InventoryClientMapper {

    public static InventoryRequest orderItemModelToInventoryRequest(OrderItemModel orderItemModel, InventoryRequest inventoryRequest) {
        inventoryRequest.setProductId(orderItemModel.getProductId());
        inventoryRequest.setQuantity(orderItemModel.getQuantity());
        return inventoryRequest;
    }
}
