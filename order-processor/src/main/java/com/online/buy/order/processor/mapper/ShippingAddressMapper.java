package com.online.buy.order.processor.mapper;

import com.online.buy.common.code.entity.AddressEntity;
import com.online.buy.order.processor.dto.ShippingDetailsDto;
import com.online.buy.order.processor.model.ShippingDetailsModel;

public class ShippingAddressMapper {

    public static ShippingDetailsModel dtoToMapper(ShippingDetailsDto shippingDetailsDto, ShippingDetailsModel shippingDetailsModel) {
        shippingDetailsModel.setStreet1(shippingDetailsDto.getStreet1());
        shippingDetailsModel.setStreet2(shippingDetailsDto.getStreet2());
        shippingDetailsModel.setLandmark(shippingDetailsDto.getLandmark());
        shippingDetailsModel.setCity(shippingDetailsDto.getCity());
        shippingDetailsModel.setPostalCode(shippingDetailsDto.getPostalCode());
        shippingDetailsModel.setState(shippingDetailsDto.getState());
        shippingDetailsModel.setCountry(shippingDetailsDto.getCountry());
        return shippingDetailsModel;
    }


    public static AddressEntity modelToEntity(ShippingDetailsModel shippingDetailsDto, AddressEntity addressEntity) {
        addressEntity.setStreet1(shippingDetailsDto.getStreet1());
        addressEntity.setStreet2(shippingDetailsDto.getStreet2());
        addressEntity.setLandmark(shippingDetailsDto.getLandmark());
        addressEntity.setCity(shippingDetailsDto.getCity());
        addressEntity.setPostalCode(shippingDetailsDto.getPostalCode());
        addressEntity.setState(shippingDetailsDto.getState());
        addressEntity.setCountry(shippingDetailsDto.getCountry());
        return addressEntity;
    }

    public static ShippingDetailsModel entityToModel(AddressEntity addressEntity, ShippingDetailsModel shippingDetailsDto) {
        shippingDetailsDto.setStreet1(addressEntity.getStreet1());
        shippingDetailsDto.setStreet2(addressEntity.getStreet2());
        shippingDetailsDto.setLandmark(addressEntity.getLandmark());
        shippingDetailsDto.setCity(addressEntity.getCity());
        shippingDetailsDto.setPostalCode(addressEntity.getPostalCode());
        shippingDetailsDto.setState(addressEntity.getState());
        shippingDetailsDto.setCountry(addressEntity.getCountry());
        return shippingDetailsDto;
    }

    public static ShippingDetailsDto modelToDto(ShippingDetailsModel shippingDetailsModel, ShippingDetailsDto shippingDetailsDto) {
        shippingDetailsDto.setStreet1(shippingDetailsModel.getStreet1());
        shippingDetailsDto.setStreet2(shippingDetailsModel.getStreet2());
        shippingDetailsDto.setLandmark(shippingDetailsModel.getLandmark());
        shippingDetailsDto.setCity(shippingDetailsModel.getCity());
        shippingDetailsDto.setPostalCode(shippingDetailsModel.getPostalCode());
        shippingDetailsDto.setState(shippingDetailsModel.getState());
        shippingDetailsDto.setCountry(shippingDetailsModel.getCountry());
        return shippingDetailsDto;
    }
}
