package com.online.buy.registration.processor.controller;

import com.online.buy.registration.processor.dto.PaymentMethodDto;
import com.online.buy.registration.processor.mapper.PaymentMethodMapper;
import com.online.buy.registration.processor.model.PaymentMethodModel;
import com.online.buy.registration.processor.service.PaymentMethodService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class RegisterPaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @PostMapping("/{userId}/register/payment-method")
    public ResponseEntity<String> registerPaymentMethod(@PathVariable("userId") @NotNull String userId, @RequestBody @Valid PaymentMethodDto paymentMethodDto) {

        paymentMethodService.registerPaymentMethod(userId, PaymentMethodMapper.dtoToModel(paymentMethodDto, new PaymentMethodModel()));
        return ResponseEntity.ok("Payment method register successfully ..!!");
    }
}
