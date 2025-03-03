package com.online.buy.inventory.processor.service;

import com.online.buy.inventory.processor.model.OrderModel;

import java.util.List;

public interface InventoryService {

    OrderModel updateInventoryIfAvailable(OrderModel orderModel);
    void updateReservation(List<Long> reservationIds);
}
