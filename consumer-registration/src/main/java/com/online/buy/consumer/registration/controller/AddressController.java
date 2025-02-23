package com.online.buy.consumer.registration.controller;

import com.online.buy.consumer.registration.domain.AddressModel;
import com.online.buy.consumer.registration.dto.AddressDTO;
import com.online.buy.consumer.registration.mapper.AddressMapper;
import com.online.buy.consumer.registration.service.ConsumerAddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consumers/{consumerId}/addresses")
public class AddressController {

    private final ConsumerAddressService consumerAddressService;

    public AddressController(ConsumerAddressService consumerAddressService) {
        this.consumerAddressService = consumerAddressService;
    }

    @PostMapping
    public ResponseEntity<AddressDTO> addAddressToConsumer(@PathVariable("consumerId") @NotNull Long consumerId,
                                                           @RequestBody @Valid AddressDTO addressDTO) {
        AddressModel addressModel = new AddressModel();
        AddressMapper.dtoToModel(addressDTO, addressModel);
        return ResponseEntity.ok(AddressMapper.modelToDto(consumerAddressService.addAddressToConsumer(consumerId, addressModel), addressDTO));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDTO> updateAddressForConsumer(@PathVariable("consumerId") @NotNull Long consumerId,
                                                               @PathVariable("addressId") @NotNull Long addressId,
                                                               @RequestBody @Valid AddressDTO addressDTO) {
        AddressModel addressModel = new AddressModel();
        AddressMapper.dtoToModel(addressDTO, addressModel);
        return ResponseEntity.ok(AddressMapper.modelToDto(consumerAddressService.updateAddressForConsumer(consumerId, addressId, addressModel), addressDTO));
    }

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAddressesForConsumer(@PathVariable("consumerId") long consumerId) {
        List<AddressModel> addressModels = consumerAddressService.findAddressForConsumer(consumerId);
        List<AddressDTO> addressDTOS = addressModels.stream()
                .map(addressModel -> AddressMapper.modelToDto(addressModel, new AddressDTO()))
                .toList();
        return ResponseEntity.ok(addressDTOS);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDTO> getAddressDetails(@PathVariable("consumerId") @NotNull Long consumerId,
                                                        @PathVariable("addressId") @NotNull Long addressId) {
        return ResponseEntity.ok(AddressMapper.modelToDto(consumerAddressService.findAddressForConsumer(consumerId, addressId), new AddressDTO()));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddressForConsumer(@PathVariable("consumerId") @NotNull Long consumerId,
                                                           @PathVariable("addressId") @NotNull Long addressId) {
        consumerAddressService.deleteAddressForConsumer(consumerId, addressId);
        return ResponseEntity.ok("Address deleted successfully!");
    }
}