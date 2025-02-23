package com.online.buy.consumer.registration.service.implementation;

import com.online.buy.consumer.registration.domain.AddressModel;
import com.online.buy.consumer.registration.entity.AddressEntity;
import com.online.buy.consumer.registration.entity.ConsumerEntity;
import com.online.buy.consumer.registration.exception.DataNotFoundException;
import com.online.buy.consumer.registration.mapper.AddressMapper;
import com.online.buy.consumer.registration.repository.AddressRepository;
import com.online.buy.consumer.registration.repository.ConsumerRepository;
import com.online.buy.consumer.registration.service.ConsumerAddressService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsumerAddressServiceImpl implements ConsumerAddressService {

    private final ConsumerRepository consumerRepository;
    private final AddressRepository addressRepository;

    public ConsumerAddressServiceImpl(ConsumerRepository consumerRepository, AddressRepository addressRepository) {
        this.consumerRepository = consumerRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public AddressModel registerConsumerAddress(Long consumerId, AddressModel addressModel) {

        ConsumerEntity consumerEntity = getConsumerEntity(consumerId);
        AddressEntity addressEntity = new AddressEntity();
        AddressMapper.modelToEntity(addressModel, addressEntity);
        addressEntity.setConsumer(consumerEntity);
        addressRepository.save(addressEntity);
        return AddressMapper.entityToModel(addressEntity, addressModel);
    }

    @Override
    public List<AddressModel> findConsumerAddress(Long consumerId) {
        ConsumerEntity consumerEntity = getConsumerEntity(consumerId);
        return consumerEntity.getAddresses().stream()
                .map(addressEntity -> AddressMapper.entityToModel(addressEntity, new AddressModel()))
                .collect(Collectors.toList());
    }

    @Override
    public AddressModel findConsumerAddress(Long consumerId, Long addressId) {
        AddressEntity addressEntity = getAddressEntity(consumerId, addressId);
        return AddressMapper.entityToModel(addressEntity, new AddressModel());
    }

    @Override
    public void deleteConsumerAddress(Long consumerId, Long addressId) {
        AddressEntity addressEntity = getAddressEntity(consumerId, addressId);
        addressRepository.delete(addressEntity);
    }

    private ConsumerEntity getConsumerEntity(Long consumerId) {
        Optional<ConsumerEntity> consumerEntityOptional = consumerRepository.findById(consumerId);
        if (consumerEntityOptional.isEmpty()) {
            throw new DataNotFoundException(HttpStatus.NOT_FOUND,
                    String.format("Consumer record not available for Consumer Id %s", consumerId),
                    String.format("Consumer record not available for Consumer %s", consumerId));
        }

        return consumerEntityOptional.get();
    }

    private AddressEntity getAddressEntity(Long consumerId, Long addressId) {
        ConsumerEntity consumerEntity = getConsumerEntity(consumerId);

        return consumerEntity.getAddresses().stream()
                .filter(address -> address.getAddressId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException(HttpStatus.NOT_FOUND,
                        String.format("Address record not available for Consumer Id %s", consumerId),
                        String.format("Address record not available for Consumer %s", consumerId)));
    }

    @Override
    public AddressModel updateConsumerAddress(Long consumerId, Long addressId, AddressModel addressModel) {
        AddressEntity addressEntity = getAddressEntity(consumerId, addressId);
        AddressMapper.modelToEntity(addressModel, addressEntity);
        addressRepository.save(addressEntity);
        return AddressMapper.entityToModel(addressEntity, addressModel);
    }
}
