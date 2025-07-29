package com.online.buy.payment.processor.controller;

import com.online.buy.payment.processor.model.PaymentOptionModel;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

// PaymentController.java
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @PostConstruct
    public void init() {
        Stripe.apiKey = "sk_test_51N0gAQSImbQaFCyxpAQHNzCo8zvvl02Z355081O5MF6LUFEVfEAuRswUmDJgFsdByYIFTA6z3A79Q8DKt1bETf8f00WalzWZWX"; // 🔐 Your Stripe Secret Key (Get from dashboard)
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody PaymentOptionModel option) throws StripeException {
        // Prepare Stripe session params
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/payment-success")
                .setCancelUrl("http://localhost:4200/payment-cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(option.getTotalAmount().multiply(BigDecimal.valueOf(100)).longValue()) // in paise
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Health Insurance - " + option.getNumberOfInstallment() + " Installments")
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        com.stripe.model.checkout.Session session = Session.create(params);

        Map<String, String> response = new HashMap<>();
        response.put("sessionId", session.getId());
        return ResponseEntity.ok(response);
    }
}
