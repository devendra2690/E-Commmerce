package com.online.buy.consumer.registration.controller;

import com.online.buy.consumer.registration.domain.AddressModel;
import com.online.buy.consumer.registration.domain.ConsumerModel;
import com.online.buy.consumer.registration.dto.AddressDTO;
import com.online.buy.consumer.registration.dto.ConsumerDto;
import com.online.buy.consumer.registration.mapper.AddressMapper;
import com.online.buy.consumer.registration.mapper.ConsumerMapper;
import com.online.buy.consumer.registration.service.ConsumerAddressService;
import com.online.buy.consumer.registration.service.ConsumerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    private final ConsumerService consumerService;
    private final ConsumerAddressService consumerAddressService;

    public ConsumerController(ConsumerService consumerService, ConsumerAddressService consumerAddressService) {
        this.consumerService = consumerService;
        this.consumerAddressService = consumerAddressService;
    }

    @PostMapping("/registration")
    public ResponseEntity<ConsumerDto> consumerRegistration(@RequestBody @Valid ConsumerDto consumerDto) {
        ConsumerModel consumerModel = new ConsumerModel();
        ConsumerMapper.dtoToModelMapper(consumerDto, consumerModel);
        return ResponseEntity.ok(ConsumerMapper.modelToDtoMapper(consumerService.registerConsumer(consumerModel), consumerDto));
    }

    @PostMapping("/registration/{consumerId}")
    public ResponseEntity<ConsumerDto> updateConsumer(@PathVariable("consumerId") @NotNull Long consumerId
                                                     , @RequestBody @Valid ConsumerDto consumerDto) {
        ConsumerModel consumerModel = new ConsumerModel();
        ConsumerMapper.dtoToModelMapper(consumerDto, consumerModel);
        return ResponseEntity.ok(ConsumerMapper.modelToDtoMapper(consumerService.updateConsumer(consumerId, consumerModel), consumerDto));
    }

    @GetMapping("/{consumerId}")
    public ResponseEntity<ConsumerDto> getConsumerDetails(@PathVariable("consumerId") long consumerId) {
        return ResponseEntity.ok(ConsumerMapper.modelToDtoMapper(consumerService.findConsumer(consumerId), new ConsumerDto()));
    }

    @DeleteMapping("/{consumerId}")
    public ResponseEntity<String> deleteConsumer(@PathVariable("consumerId") long consumerId) {
        consumerService.deleteConsumer(consumerId);
        return ResponseEntity.ok("User deleted successfully!");
    }

    @PostMapping("{consumerId}/registration/address")
    public ResponseEntity<AddressDTO> registerConsumerAddress(@RequestBody @Valid AddressDTO addressDTO
                                                              , @PathVariable("consumerId") @NotNull Long consumerId) {
        AddressModel addressModel = new AddressModel();
        AddressMapper.dtoToModel(addressDTO, addressModel);
        return ResponseEntity.ok(AddressMapper.modelToDto(consumerAddressService.registerConsumerAddress(consumerId, addressModel), addressDTO));
    }

    @PostMapping("/{consumerId}/registration/address/{addressId}")
    public ResponseEntity<AddressDTO> updateConsumerAddress(@PathVariable("consumerId") @NotNull Long consumerId
                                                            , @PathVariable("addressId") @NotNull Long addressId
                                                            , @RequestBody @Valid AddressDTO addressDTO) {
        AddressModel addressModel = new AddressModel();
        AddressMapper.dtoToModel(addressDTO, addressModel);
        return ResponseEntity.ok(AddressMapper.modelToDto(consumerAddressService.updateConsumerAddress(consumerId, addressId, addressModel), addressDTO));
    }

    @GetMapping("/{consumerId}/address")
    public ResponseEntity<List<AddressDTO>> getConsumerAddressDetails(@PathVariable("consumerId") long consumerId) {

        List<AddressModel> addressModels = consumerAddressService.findConsumerAddress(consumerId);
        List<AddressDTO> addressDTOS = addressModels.stream().map(addressModel -> {
            AddressDTO addressDTO = new AddressDTO();
            return AddressMapper.modelToDto(addressModel, addressDTO);
        }).toList();

        return ResponseEntity.ok(addressDTOS);
    }

    @GetMapping("/{consumerId}/address/{addressId}")
    public ResponseEntity<AddressDTO> getAddressDetailsById(@PathVariable("consumerId") @NotNull Long consumerId
                                                                      ,@PathVariable("addressId") @NotNull Long addressId) {

        return ResponseEntity.ok(AddressMapper.modelToDto(consumerAddressService.findConsumerAddress(consumerId, addressId), new AddressDTO()));
    }

    @DeleteMapping("/{consumerId}/address/{addressId}")
    public ResponseEntity<String> deleteConsumerAddress(@PathVariable("consumerId") @NotNull Long consumerId
                                                        ,@PathVariable("addressId") @NotNull Long addressId) {
        consumerAddressService.deleteConsumerAddress(consumerId, addressId);
        return ResponseEntity.ok("User Address deleted successfully!");
    }
}