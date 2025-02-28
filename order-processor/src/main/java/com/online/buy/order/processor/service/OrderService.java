package com.online.buy.order.processor.service;

import com.online.buy.order.processor.model.OrderModel;

public interface OrderService {
    OrderModel processOrder(OrderModel orderModel);
}
