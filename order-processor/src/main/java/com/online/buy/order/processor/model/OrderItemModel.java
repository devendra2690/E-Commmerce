package com.online.buy.order.processor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemModel {

    private Long clientId;
    private Long productId;
    private int quantity;
    private double price;  // Price per unit
}
