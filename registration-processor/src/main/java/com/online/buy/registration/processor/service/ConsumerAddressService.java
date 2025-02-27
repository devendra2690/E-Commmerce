package com.online.buy.registration.processor.service;



import com.online.buy.registration.processor.model.AddressModel;

import java.util.List;

public interface ConsumerAddressService {

    AddressModel addAddressToConsumer(String userId, AddressModel addressModel);
    List<AddressModel> findAddressForConsumer(String userId);
    AddressModel findAddressForConsumer(String userId, Long addressId);
    void deleteAddressForConsumer(String userId, Long addressId);
    AddressModel updateAddressForConsumer(String userId, Long addressId, AddressModel consumerModel);
}
