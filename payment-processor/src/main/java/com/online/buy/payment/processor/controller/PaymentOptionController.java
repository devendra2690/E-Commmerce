package com.online.buy.payment.processor.controller;

import com.online.buy.payment.processor.dto.PaymentOptionRequestDto;
import com.online.buy.payment.processor.dto.PaymentOptionsResponseDto;
import com.online.buy.payment.processor.service.PaymentOptionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/payment-options")
@AllArgsConstructor
public class PaymentOptionController {

    private final PaymentOptionService paymentOptionService;

    @PostMapping
    public PaymentOptionsResponseDto getPaymentOptions(@RequestBody PaymentOptionRequestDto paymentOptionRequestDto) {

        return new PaymentOptionsResponseDto(paymentOptionRequestDto.getUserId(),
                paymentOptionService.getPaymentOptions(paymentOptionRequestDto.getUserId(), paymentOptionRequestDto.getAmount()));
    }
}
