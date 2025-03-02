package com.online.buy.registration.processor.mapper;

import com.online.buy.registration.processor.dto.ConsumerDto;
import com.online.buy.registration.processor.model.ConsumerModel;

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
}
