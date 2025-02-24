package com.online.buy.registration.processor.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressModel {

    private Long addressId;
    private String street1;
    private String street2;
    private String landmark;
    private String city;
    private String postalCode;
    private String state;
    private String country;
}
