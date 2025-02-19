package com.online.buy.consumer.registration.controller;

import com.online.buy.consumer.registration.domain.ConsumerModel;
import com.online.buy.consumer.registration.dto.ConsumerDto;
import com.online.buy.consumer.registration.mapper.ObjectMapperUtil;
import com.online.buy.consumer.registration.service.ConsumerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consumer")
@PreAuthorize("hasRole('BUYER')")
public class ConsumerController {

    private final ConsumerService consumerService;

    public ConsumerController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> consumerRegistration(@RequestBody @Valid ConsumerDto consumerDto) {

        ConsumerModel consumerModel = ObjectMapperUtil.map(consumerDto,ConsumerModel.class);
        consumerService.registerConsumer(consumerModel);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsumerDto> getConsumerDetails(@PathVariable("id") long consumerId) {
        ConsumerModel consumerModel = consumerService.findConsumer(consumerId);
        ConsumerDto consumerDto = ObjectMapperUtil.map(consumerModel, ConsumerDto.class);
        return ResponseEntity.ok(consumerDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConsumer(@PathVariable("id") long consumerId) {
        consumerService.deleteConsumer(consumerId);
        return ResponseEntity.ok("User deleted successfully ..!!");
    }
}
