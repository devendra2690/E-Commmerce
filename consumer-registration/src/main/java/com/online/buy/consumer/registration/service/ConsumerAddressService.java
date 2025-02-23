package com.online.buy.consumer.registration.service;

import com.online.buy.consumer.registration.domain.AddressModel;

import java.util.List;

public interface ConsumerAddressService {

    AddressModel registerConsumerAddress(Long consumerId, AddressModel addressModel);
    List<AddressModel> findConsumerAddress(Long consumerId);
    AddressModel findConsumerAddress(Long consumerId, Long addressId);
    void deleteConsumerAddress(Long consumerId, Long addressId);
    AddressModel updateConsumerAddress(Long consumerId, Long addressId, AddressModel consumerModel);
}
