package com.online.buy.order.processor.helper;

import com.online.buy.order.processor.model.ShippingDetailsModel;
import org.springframework.util.DigestUtils;

public class AddressHelper {

    public static String generateAddressHash(ShippingDetailsModel shippingDetails) {
        String normalizedAddress = String.join("|",
                normalizeStreet(shippingDetails.getStreet1()),
                normalizeStreet(shippingDetails.getStreet2()),
                shippingDetails.getCity().toLowerCase().trim(),
                shippingDetails.getState().toLowerCase().trim(),
                shippingDetails.getPostalCode().trim(),
                shippingDetails.getCountry().toLowerCase().trim()
        );
        return DigestUtils.md5DigestAsHex(normalizedAddress.getBytes());
    }

    private static String normalizeStreet(String street) {
        if (street == null) return "";
        return street.toLowerCase().trim()
                .replace(" street", " st.")
                .replace(" road", " rd.")
                .replace(" avenue", " ave.")
                .replace(" boulevard", " blvd.")
                .replace(" lane", " ln.")
                .replace(" drive", " dr.");
    }
}
