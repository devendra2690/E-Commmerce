package com.online.buy.registration.processor.service.impl;

import com.online.buy.common.code.entity.User;
import com.online.buy.common.code.service.EncryptionServiceImpl;
import com.online.buy.registration.processor.model.PaymentMethodModel;
import com.online.buy.registration.processor.service.PaymentMethodService;
import com.online.buy.registration.processor.service.UserService;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentMethodAttachParams;
import com.stripe.param.PaymentMethodCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final UserService userService;
    private final EncryptionServiceImpl encryptionService;


    @Override
    public void registerPaymentMethod(String userId, PaymentMethodModel paymentMethodModel) {

        User user = userService.findById(userId);

        Customer customer = null;
        try {
            customer = Customer.create(CustomerCreateParams.builder()
                    .setEmail(user.getEmail())
                    .setName(user.getUsername())
                    .build());

            String customerId = customer.getId();

            PaymentMethod paymentMethod = PaymentMethod.create(PaymentMethodCreateParams.builder()
                .setType(PaymentMethodCreateParams.Type.CARD)
                .setCard(PaymentMethodCreateParams.CardDetails.builder()
                        .setNumber(paymentMethodModel.getCardNumber()) // Test Card
                        .setExpMonth(paymentMethodModel.getExpMoth())
                        .setExpYear(paymentMethodModel.getExpYear())
                        .setCvc(paymentMethodModel.getCvc())
                        .build())
                .build());

        paymentMethod.attach(PaymentMethodAttachParams.builder()
                .setCustomer(customerId)
                .build());

        String encryptedCustomerId = encryptionService.encrypt(customerId);
        user.setStripeCustId(encryptedCustomerId);
        userService.updateUser(user);

        } catch (Exception e) {
            throw new RuntimeException("Error occured while creating payment method",e);
        }
    }

   /* public static void main(String[] args) throws Exception {
        // Generate AES key
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // 256-bit key
        SecretKey secretKey = keyGenerator.generateKey();

        // Convert key to Base64
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        System.out.println("Generated Base64 AES Key: " + base64Key);
    }*/
}
