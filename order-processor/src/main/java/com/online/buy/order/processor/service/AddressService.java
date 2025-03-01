package com.online.buy.order.processor.service;

import com.online.buy.common.code.entity.AddressEntity;
import com.online.buy.common.code.entity.User;
import com.online.buy.order.processor.model.ShippingDetailsModel;

import java.util.Optional;

public interface AddressService {
   Optional<AddressEntity> findByAddressHashAndUserId(String addressHash, String userId);
   AddressEntity saveAddressEntity(ShippingDetailsModel shippingDetailsModel, User user, String userId, String addressHash);

}
