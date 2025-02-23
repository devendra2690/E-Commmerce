package com.online.buy.consumer.registration.mapper;

import com.online.buy.consumer.registration.domain.ConsumerModel;
import com.online.buy.consumer.registration.dto.ConsumerDto;
import com.online.buy.consumer.registration.entity.ConsumerEntity;

public class ConsumerMapper {

    public static ConsumerModel dtoToModelMapper(ConsumerDto consumerDto, ConsumerModel consumerModel) {

        consumerModel.setFirstName(consumerDto.getFirstName());
        consumerModel.setLastName(consumerDto.getLastName());
        consumerModel.setPassword(consumerDto.getPassword());
        consumerModel.setEmail(consumerDto.getEmail());
        consumerModel.setPhoneNumber(consumerDto.getPhoneNumber());

        return consumerModel;
    }

    public static ConsumerDto modelToDtoMapper(ConsumerModel consumerModel, ConsumerDto consumerDto) {

        consumerDto.setConsumerId(consumerModel.getConsumerId());
        consumerDto.setFirstName(consumerModel.getFirstName());
        consumerDto.setLastName(consumerModel.getLastName());
        //consumerDto.setPassword(consumerModel.getPassword());
        consumerDto.setEmail(consumerModel.getEmail());
        consumerDto.setPhoneNumber(consumerModel.getPhoneNumber());

        return consumerDto;
    }

    public static ConsumerEntity modelToEntityMapper(ConsumerModel consumerModel, ConsumerEntity consumerEntity) {
        consumerEntity.setFirstName(consumerModel.getFirstName());
        consumerEntity.setLastName(consumerModel.getLastName());
        consumerEntity.setEmail(consumerModel.getEmail());
        consumerEntity.setPhoneNumber(consumerModel.getPhoneNumber());

        return consumerEntity;
    }

    public static ConsumerModel entityToModelMapper(ConsumerEntity consumerEntity, ConsumerModel consumerModel) {
        consumerModel.setConsumerId(consumerEntity.getConsumerId());
        consumerModel.setFirstName(consumerEntity.getFirstName());
        consumerModel.setLastName(consumerEntity.getLastName());
        consumerModel.setEmail(consumerEntity.getEmail());
        consumerModel.setPhoneNumber(consumerEntity.getPhoneNumber());

        return consumerModel;
    }
}
