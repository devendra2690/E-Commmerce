package com.online.buy.order.processor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.online.buy.order.processor.enums.PaymentMode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long orderId;

    @JsonProperty("userId")
    @NotNull(message = "User Id cannot be null")
    private String userId;         // ID of the buyer

    @JsonProperty("email")
    @NotNull(message = "email cannot be null")
    private String email;          // Buyer's email

    @JsonProperty("items")
    @Size(min=1, message="At least one order should be places")
    @Valid
    private List<OrderItemDto> items;  // List of products in the order

    @JsonProperty("paymentMode")
    @NotNull(message = "Payment Mode can not be null")
    private PaymentMode paymentMode;  // COD, UPI, Credit Card, etc.

    @JsonProperty("shippingDetails")
    @Valid
    private ShippingDetailsDto shippingDetails; // Shipping details
}

