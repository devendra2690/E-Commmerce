package com.online.buy.order.processor.model;

import com.online.buy.order.processor.dto.ShippingDetailsDto;
import com.online.buy.order.processor.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel {

    private Long orderId;
    private String userId;         // ID of the buyer
    private String email;          // Buyer's email
    private List<OrderItemModel> items;  // List of products in the order
    private PaymentMode paymentMode;  // COD, UPI, Credit Card, etc.
    private ShippingDetailsModel shippingDetails; // Shipping details
    private Map<Long, Map<String, Object>> additionalInfo;
}
