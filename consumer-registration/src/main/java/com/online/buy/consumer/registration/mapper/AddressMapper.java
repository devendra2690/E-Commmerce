package com.online.buy.consumer.registration.mapper;

import com.online.buy.consumer.registration.domain.AddressModel;
import com.online.buy.consumer.registration.dto.AddressDTO;
import com.online.buy.consumer.registration.entity.AddressEntity;

public class AddressMapper {

    public static AddressModel dtoToModel(AddressDTO addressDTO,AddressModel addressModel) {

        addressModel.setStreet1(addressDTO.getStreet1());
        addressModel.setStreet2(addressDTO.getStreet2());
        addressModel.setLandmark(addressDTO.getLandmark());
        addressModel.setPostalCode(addressDTO.getPostalCode());
        addressModel.setState(addressDTO.getState());
        addressModel.setCountry(addressDTO.getCountry());
        addressModel.setCity(addressDTO.getCity());
        return addressModel;
    }

    public static AddressDTO modelToDto(AddressModel addressModel, AddressDTO addressDTO) {

        addressDTO.setAddressId(addressModel.getAddressId());
        addressDTO.setStreet1(addressModel.getStreet1());
        addressDTO.setStreet2(addressModel.getStreet2());
        addressDTO.setLandmark(addressModel.getLandmark());
        addressDTO.setPostalCode(addressModel.getPostalCode());
        addressDTO.setState(addressModel.getState());
        addressDTO.setCountry(addressModel.getCountry());
        addressDTO.setCity(addressModel.getCity());
        return addressDTO;
    }

    public static AddressEntity modelToEntity(AddressModel addressModel, AddressEntity addressEntity) {

        addressEntity.setStreet1(addressModel.getStreet1());
        addressEntity.setStreet2(addressModel.getStreet2());
        addressEntity.setLandmark(addressModel.getLandmark());
        addressEntity.setPostalCode(addressModel.getPostalCode());
        addressEntity.setState(addressModel.getState());
        addressEntity.setCountry(addressModel.getCountry());
        addressEntity.setCity(addressModel.getCity());
        return addressEntity;
    }

    public static AddressModel entityToModel(AddressEntity addressEntity, AddressModel addressModel) {

        addressModel.setAddressId(addressEntity.getAddressId());
        addressModel.setStreet1(addressEntity.getStreet1());
        addressModel.setStreet2(addressEntity.getStreet2());
        addressModel.setLandmark(addressEntity.getLandmark());
        addressModel.setPostalCode(addressEntity.getPostalCode());
        addressModel.setState(addressEntity.getState());
        addressModel.setCountry(addressEntity.getCountry());
        addressModel.setCity(addressEntity.getCity());

        return addressModel;
    }
}
