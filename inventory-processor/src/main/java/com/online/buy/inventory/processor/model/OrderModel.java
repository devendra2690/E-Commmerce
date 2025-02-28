package com.online.buy.inventory.processor.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderModel {

    private String userId;
    private List<OrderItemModel> orderItemModelList;
}
