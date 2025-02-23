package com.online.buy.consumer.registration.service;

import com.online.buy.consumer.registration.domain.AddressModel;

import java.util.List;

public interface ConsumerAddressService {

    AddressModel addAddressToConsumer(Long consumerId, AddressModel addressModel);
    List<AddressModel> findAddressForConsumer(Long consumerId);
    AddressModel findAddressForConsumer(Long consumerId, Long addressId);
    void deleteAddressForConsumer(Long consumerId, Long addressId);
    AddressModel updateAddressForConsumer(Long consumerId, Long addressId, AddressModel consumerModel);
}
