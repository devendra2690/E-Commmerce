package com.online.buy.order.processor.controller;

import com.online.buy.order.processor.dto.OrderDto;
import com.online.buy.order.processor.mapper.OrderMapper;
import com.online.buy.order.processor.model.OrderModel;
import com.online.buy.order.processor.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderProcessorController {

    private final OrderService orderService;

    @PostMapping("/create-order")
    public ResponseEntity<OrderDto> createOrder(@RequestBody @Valid OrderDto orderDto) {
        return ResponseEntity.ok(OrderMapper.modelToDto(orderService.processOrder(OrderMapper.dtpToModel(orderDto, new OrderModel())), orderDto));
    }
}
