package com.online.buy.consumer.registration.controller;

import com.online.buy.consumer.registration.domain.ConsumerModel;
import com.online.buy.consumer.registration.dto.ConsumerDto;
import com.online.buy.consumer.registration.mapper.ConsumerMapper;
import com.online.buy.consumer.registration.service.ConsumerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consumers")
public class ConsumerController {

    private final ConsumerService consumerService;

    public ConsumerController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @PostMapping
    public ResponseEntity<ConsumerDto> registerConsumer(@RequestBody @Valid ConsumerDto consumerDto) {
        ConsumerModel consumerModel = new ConsumerModel();
        ConsumerMapper.dtoToModelMapper(consumerDto, consumerModel);
        return ResponseEntity.ok(ConsumerMapper.modelToDtoMapper(consumerService.registerConsumer(consumerModel), consumerDto));
    }

    @PutMapping("/{consumerId}")
    public ResponseEntity<ConsumerDto> updateConsumer(@PathVariable("consumerId") @NotNull Long consumerId,
                                                      @RequestBody @Valid ConsumerDto consumerDto) {
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
}