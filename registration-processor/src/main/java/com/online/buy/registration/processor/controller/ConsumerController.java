package com.online.buy.registration.processor.controller;

import com.online.buy.registration.processor.dto.ConsumerDto;
import com.online.buy.registration.processor.mapper.ConsumerMapper;
import com.online.buy.registration.processor.model.ConsumerModel;
import com.online.buy.registration.processor.service.ConsumerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consumers")
@AllArgsConstructor
public class ConsumerController {

    private final ConsumerService consumerService;

    @PostMapping("/register-consumer")
    public ResponseEntity<ConsumerDto> registerConsumer(@RequestBody @Valid ConsumerDto consumerDto) {
        return ResponseEntity.ok(ConsumerMapper.modelToDtoMapper(consumerService.registerConsumer(
                ConsumerMapper.dtoToModelMapper(consumerDto, new ConsumerModel())), consumerDto));
    }

    @PutMapping("/update-consumer/{consumerId}")
    public ResponseEntity<ConsumerDto> updateConsumer(@PathVariable("consumerId") @NotNull Long consumerId,
                                                      @RequestBody @Valid ConsumerDto consumerDto) {
        return ResponseEntity.ok(ConsumerMapper.modelToDtoMapper(consumerService.updateConsumer(consumerId,
                ConsumerMapper.dtoToModelMapper(consumerDto, new ConsumerModel())), consumerDto));
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