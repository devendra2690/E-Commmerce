package com.online.buy.inventory.processor.service;

import com.online.buy.inventory.processor.model.OrderModel;

public interface InventoryService {

    OrderModel updateInventoryIfAvailable(OrderModel orderModel) ;
}
