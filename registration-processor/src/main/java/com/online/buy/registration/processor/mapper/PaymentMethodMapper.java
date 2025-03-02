package com.online.buy.registration.processor.mapper;

import com.online.buy.registration.processor.dto.PaymentMethodDto;
import com.online.buy.registration.processor.model.PaymentMethodModel;

public class PaymentMethodMapper {

    public static PaymentMethodModel dtoToModel(PaymentMethodDto paymentMethodDto, PaymentMethodModel paymentMethodModel) {
        paymentMethodModel.setCvc(paymentMethodDto.getCvc());
        paymentMethodModel.setCardNumber(paymentMethodDto.getCardNumber());
        paymentMethodModel.setExpMoth(paymentMethodDto.getExpMoth());
        paymentMethodModel.setExpYear(paymentMethodDto.getExpYear());
        return paymentMethodModel;
    }
}
