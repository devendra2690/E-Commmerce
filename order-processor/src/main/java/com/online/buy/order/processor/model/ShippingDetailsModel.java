package com.online.buy.order.processor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDetailsModel {

    private String street1;
    private String street2; // Optional field
    private String landmark; // Optional field
    private String city;
    private String postalCode;
    private String state;
    private String country;
}
