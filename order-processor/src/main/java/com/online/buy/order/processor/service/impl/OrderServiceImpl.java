package com.online.buy.order.processor.service.impl;

import com.online.buy.common.code.dto.inventory.InventoryClientDto;
import com.online.buy.common.code.dto.inventory.InventoryItemClientDto;
import com.online.buy.common.code.entity.AddressEntity;
import com.online.buy.common.code.entity.Client;
import com.online.buy.common.code.entity.Product;
import com.online.buy.common.code.entity.User;
import com.online.buy.common.code.repository.ClientRepository;
import com.online.buy.common.code.repository.ProductRepository;
import com.online.buy.exception.processor.model.NotFoundException;
import com.online.buy.order.processor.client.InventoryClient;
import com.online.buy.order.processor.entity.Order;
import com.online.buy.order.processor.entity.OrderItem;
import com.online.buy.order.processor.enums.OrderStatus;
import com.online.buy.order.processor.mapper.InventoryClientMapper;
import com.online.buy.order.processor.mapper.OrderItemMapper;
import com.online.buy.order.processor.mapper.OrderMapper;
import com.online.buy.order.processor.model.OrderModel;
import com.online.buy.order.processor.model.ShippingDetailsModel;
import com.online.buy.order.processor.repository.OrderRepository;
import com.online.buy.order.processor.service.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserService userService;
    private final ClientService clientService;
    private final ProductService productService;
    private final AddressService addressService;
    private final InventoryClient inventoryClient;
    private final OrderRepository orderRepository;
    private final ReservationService reservationService;

    @Transactional
    @Override
    public OrderModel processOrder(OrderModel orderModel) {

        // validate user
        User user = userService.findById(orderModel.getUserId());

        // validate product and client combination
        List<OrderItem> orderItems = validateClientProductMapping(orderModel);

        // decide to create new address or not using addressHash
        AddressEntity addressEntity = getOrCreateShippingAddress(orderModel.getShippingDetails(), user);

        // validate inventory
        Map<String, Object> inventoryCheckResult = checkInventoryAvailability(orderModel);
        @SuppressWarnings("unchecked")
        Map<Long, Map<String, Object>> errorMap = (Map<Long, Map<String, Object>>) inventoryCheckResult.get("errorMap");

        Order order = createOrder(orderModel, user, addressEntity);

        // filter out order item which is either out of stock or reservation already exist for it (Likely duplicate order)
        orderItems.stream()
                .filter(orderItem -> !errorMap.containsKey(orderItem.getProduct().getId()))
                .forEach(order::addOrderItem);

        try {
            if (!CollectionUtils.isEmpty(order.getOrderItems())) {
                orderRepository.save(order);
            }
        } catch (Exception exception) {
            rollbackInventoryReservations(inventoryCheckResult);
            throw new HttpServerErrorException(HttpStatusCode.valueOf(500), "Error while saving order, reverting reservations");
        }

        orderModel.setAdditionalInfo(errorMap);
        return OrderMapper.entityToModel(order, orderModel);
    }

    private Map<String, Object> checkInventoryAvailability(OrderModel orderModel) {
        InventoryClientDto inventoryRequest = new InventoryClientDto();
        inventoryRequest.setUserId(orderModel.getUserId());
        inventoryRequest.setInventoryItemClientDtos(orderModel.getItems().stream()
                .map(item -> InventoryClientMapper.orderItemModelToInventoryRequest(item, new InventoryItemClientDto()))
                .collect(Collectors.toList()));

        InventoryClientDto response = inventoryClient.validateInventories(inventoryRequest);

        Map<Long, Map<String, Object>> errorMap = response.getInventoryItemClientDtos().stream()
                .filter(item -> !item.isSuccess())
                .collect(Collectors.toMap(
                        InventoryItemClientDto::getProductId,
                        item -> Map.of(
                                "message", item.getMessage(),
                                "productId", item.getProductId()
                        )
                ));

        return Map.of("inventoryClientDto", response, "errorMap", errorMap);
    }

    private List<OrderItem> validateClientProductMapping(OrderModel orderModel) {
        return orderModel.getItems().stream().map(item -> {

            Client client = clientService.findById(item.getClientId());
            Product product = productService.findByProductIdAndClientId(item.getProductId(), item.getClientId());

            OrderItem orderItem = new OrderItem();
            orderItem.setClient(client);
            orderItem.setProduct(product);
            OrderItemMapper.itemModelToEntity(item, orderItem);
            return orderItem;
        }).collect(Collectors.toList());
    }

    private AddressEntity getOrCreateShippingAddress(ShippingDetailsModel shippingDetails, User user) {
        return addressService.saveAddressEntity(shippingDetails,user,null,null);
    }

    private Order createOrder(OrderModel orderModel, User user, AddressEntity addressEntity) {
        Order order = new Order();
        order.setUser(user);
        order.setEmail(orderModel.getEmail());
        order.setOrderStatus(OrderStatus.IN_PROGRESS);
        order.setAddressEntity(addressEntity);
        order.setPaymentMode(orderModel.getPaymentMode());
        return order;
    }

    private void rollbackInventoryReservations(Map<String, Object> inventoryCheckResult) {
        InventoryClientDto inventoryClientDto = (InventoryClientDto) inventoryCheckResult.get("inventoryClientDto");
        List<Long> reservationIds = inventoryClientDto.getInventoryItemClientDtos().stream()
                .map(InventoryItemClientDto::getReservationId)
                .toList();
        reservationService.rollbackReservation(reservationIds);
    }
}
