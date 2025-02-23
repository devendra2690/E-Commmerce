package com.online.buy.consumer.registration.service.implementation;

import com.online.buy.consumer.registration.domain.AddressModel;
import com.online.buy.consumer.registration.entity.AddressEntity;
import com.online.buy.consumer.registration.entity.ConsumerEntity;
import com.online.buy.consumer.registration.exception.DataNotFoundException;
import com.online.buy.consumer.registration.mapper.AddressMapper;
import com.online.buy.consumer.registration.repository.AddressRepository;
import com.online.buy.consumer.registration.repository.ConsumerRepository;
import com.online.buy.consumer.registration.service.ConsumerAddressService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsumerAddressServiceImpl implements ConsumerAddressService {

    private final ConsumerRepository consumerRepository;
    private final AddressRepository addressRepository;

    @Override
    @Transactional
    public AddressModel addAddressToConsumer(Long consumerId, AddressModel addressModel) {
        ConsumerEntity consumerEntity = getConsumerEntity(consumerId);
        AddressEntity addressEntity = new AddressEntity();
        AddressMapper.modelToEntity(addressModel, addressEntity);
        addressEntity.setConsumer(consumerEntity);
        addressEntity = addressRepository.save(addressEntity);
        return AddressMapper.entityToModel(addressEntity, new AddressModel());
    }

    @Override
    public List<AddressModel> findAddressForConsumer(Long consumerId) {
        ConsumerEntity consumerEntity = getConsumerEntity(consumerId);
        return consumerEntity.getAddresses().stream()
                .map(addressEntity -> AddressMapper.entityToModel(addressEntity, new AddressModel()))
                .toList();
    }

    @Override
    public AddressModel findAddressForConsumer(Long consumerId, Long addressId) {
        AddressEntity addressEntity = getAddressEntity(consumerId, addressId);
        return AddressMapper.entityToModel(addressEntity, new AddressModel());
    }

    @Override
    @Transactional
    public void deleteAddressForConsumer(Long consumerId, Long addressId) {
        AddressEntity addressEntity = getAddressEntity(consumerId, addressId);
        addressRepository.delete(addressEntity);
    }

    @Override
    @Transactional
    public AddressModel updateAddressForConsumer(Long consumerId, Long addressId, AddressModel addressModel) {
        AddressEntity addressEntity = getAddressEntity(consumerId, addressId);
        AddressMapper.modelToEntity(addressModel, addressEntity);
        return AddressMapper.entityToModel(addressRepository.save(addressEntity), new AddressModel());
    }

    private ConsumerEntity getConsumerEntity(Long consumerId) {
        return consumerRepository.findById(consumerId)
                .orElseThrow(() -> new DataNotFoundException(HttpStatus.NOT_FOUND,
                        String.format("Consumer with ID %s not found", consumerId),
                        String.format("Consumer not found for ID: %s", consumerId)));
    }

    private AddressEntity getAddressEntity(Long consumerId, Long addressId) {
        return addressRepository.findById(addressId)
                .filter(address -> address.getConsumer().getConsumerId().equals(consumerId))
                .orElseThrow(() -> new DataNotFoundException(HttpStatus.NOT_FOUND,
                        String.format("Address with ID %s not found for Consumer ID %s", addressId, consumerId),
                        String.format("Address not found for Consumer ID: %s", consumerId)));
    }
}