package com.online.buy.registration.processor.model;

import lombok.Data;

@Data
public class PaymentMethodModel {

    private String cardNumber;
    private Long expMoth;
    private Long expYear;
    private String cvc;

}
