package com.online.buy.order.processor.service.impl;

import com.online.buy.common.code.entity.AddressEntity;
import com.online.buy.common.code.entity.User;
import com.online.buy.common.code.repository.AddressRepository;
import com.online.buy.order.processor.helper.AddressHelper;
import com.online.buy.order.processor.mapper.ShippingAddressMapper;
import com.online.buy.order.processor.model.ShippingDetailsModel;
import com.online.buy.order.processor.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserServiceImpl userService;


    @Override
    public Optional<AddressEntity> findByAddressHashAndUserId(String addressHash, String userId) {
        return addressRepository.findByAddressHashAndUserId(addressHash, UUID.fromString(userId));
    }

    @Override
    public AddressEntity saveAddressEntity(ShippingDetailsModel shippingDetailsModel, User user, String userId, String addressHash) {
        if(addressHash == null) {
            addressHash = AddressHelper.generateAddressHash(shippingDetailsModel);
        }
        if(user == null) {
            user = userService.findById(userId);
        }
        Optional<AddressEntity> addressEntity = addressRepository.findByAddressHashAndUserId(addressHash, user.getId());
        if(addressEntity.isEmpty()) {
            AddressEntity newAddress = ShippingAddressMapper.modelToEntity(shippingDetailsModel, new AddressEntity());
            newAddress.setUser(user);
            newAddress.setAddressHash(addressHash);
            return addressRepository.save(newAddress);
        }
        return addressEntity.get();
    }

}
