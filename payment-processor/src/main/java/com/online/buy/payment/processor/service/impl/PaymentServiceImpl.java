package com.online.buy.payment.processor.service.impl;

import com.online.buy.payment.processor.service.PaymentService;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Override
    public void chargeCustomer(String customerId, Long amount) {

        try {
            Random random = new Random();
            String paymentMethod;

            // 30% chance of failure
            if (random.nextInt(100) < 30) {
                paymentMethod = "pm_card_chargeDeclined"; // ❌ Simulated failed payment
            } else {
                paymentMethod = "pm_card_visa"; // ✅ Successful payment
            }

            // Set this key to avoid duplicate payments from user, payment gateway will use this key to make sure payment should not be made in case of retry
            String idempotencyKey = UUID.randomUUID().toString();

            RequestOptions requestOptions = RequestOptions.builder()
                    .setIdempotencyKey(idempotencyKey)
                    .build();

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(1000L) // Amount in cents (e.g., $10.00)
                    .setCurrency("usd")
                    .addPaymentMethodType("card") // Specify allowed payment method types
                    .setConfirm(true) // Auto-confirm the payment
                    .setPaymentMethod(paymentMethod) // ✅ Use Stripe test PaymentMethod ID
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params, requestOptions);
            System.out.println("PaymentIntent Status: " + paymentIntent.getStatus());
            if(paymentIntent.getStatus().equalsIgnoreCase("succeeded")) {

            }else {

            }
        }catch(Exception e) {
            throw new HttpServerErrorException(HttpStatusCode.valueOf(500), e.getMessage());
        }
    }
}
