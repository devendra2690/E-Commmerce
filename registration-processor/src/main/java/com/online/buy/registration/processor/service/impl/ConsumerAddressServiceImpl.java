package com.online.buy.registration.processor.service.impl;

import com.online.buy.common.code.entity.AddressEntity;
import com.online.buy.common.code.entity.User;
import com.online.buy.common.code.repository.AddressRepository;
import com.online.buy.common.code.repository.UserRepository;
import com.online.buy.exception.processor.model.NotFoundException;
import com.online.buy.registration.processor.mapper.AddressMapper;
import com.online.buy.registration.processor.model.AddressModel;
import com.online.buy.registration.processor.service.ConsumerAddressService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsumerAddressServiceImpl implements ConsumerAddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    @Transactional
    public AddressModel addAddressToConsumer(String userId, AddressModel addressModel) {
        User user = getUserEntity(UUID.fromString(userId));
        AddressEntity addressEntity = new AddressEntity();
        AddressMapper.modelToEntity(addressModel, addressEntity);
        addressEntity.setUser(user);
        addressEntity = addressRepository.save(addressEntity);
        return AddressMapper.entityToModel(addressEntity, new AddressModel());
    }

    @Override
    public List<AddressModel> findAddressForConsumer(String userId) {
        User user = getUserEntity(UUID.fromString(userId));
        return user.getAddresses().stream()
                .map(addressEntity -> AddressMapper.entityToModel(addressEntity, new AddressModel()))
                .toList();
    }

    @Override
    public AddressModel findAddressForConsumer(String userId, Long addressId) {
        AddressEntity addressEntity = getAddressEntity(userId, addressId);
        return AddressMapper.entityToModel(addressEntity, new AddressModel());
    }

    @Override
    @Transactional
    public void deleteAddressForConsumer(String consumerId, Long addressId) {
        AddressEntity addressEntity = getAddressEntity(consumerId, addressId);
        addressRepository.delete(addressEntity);
    }

    @Override
    public AddressModel updateAddressForConsumer(String userId, Long addressId, AddressModel addressModel) {
        AddressEntity addressEntity = getAddressEntity(userId, addressId);
        AddressMapper.modelToEntity(addressModel, addressEntity);
        return AddressMapper.entityToModel(addressRepository.save(addressEntity), new AddressModel());
    }

    private User getUserEntity(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Consumer with ID %s not found", userId)));
    }

    private AddressEntity getAddressEntity(String userId, Long addressId) {
        return addressRepository.findById(addressId)
                .filter(address -> address.getUser().getId().equals(UUID.fromString(userId)))
                .orElseThrow(() -> new NotFoundException( String.format("Address with ID %s not found for Consumer ID %s", addressId, userId)));
    }
}