package com.online.buy.order.processor.service.impl;

import com.online.buy.common.code.dto.inventory.InventoryItemClientDto;
import com.online.buy.common.code.dto.inventory.InventoryClientDto;
import com.online.buy.common.code.entity.AddressEntity;
import com.online.buy.common.code.entity.Client;
import com.online.buy.common.code.entity.Product;
import com.online.buy.common.code.entity.User;
import com.online.buy.common.code.repository.*;
import com.online.buy.exception.processor.model.NotFoundException;
import com.online.buy.order.processor.client.InventoryClient;
import com.online.buy.order.processor.entity.Order;
import com.online.buy.order.processor.entity.OrderItem;
import com.online.buy.order.processor.enums.OrderStatus;
import com.online.buy.order.processor.mapper.InventoryClientMapper;
import com.online.buy.order.processor.mapper.OrderItemMapper;
import com.online.buy.order.processor.mapper.OrderMapper;
import com.online.buy.order.processor.mapper.ShippingAddressMapper;
import com.online.buy.order.processor.model.OrderModel;
import com.online.buy.order.processor.model.ShippingDetailsModel;
import com.online.buy.order.processor.repository.OrderRepository;
import com.online.buy.order.processor.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.HttpServerErrorException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final InventoryClient inventoryClient;
    private final OrderRepository orderRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public OrderModel processOrder(OrderModel orderModel) {
        User user = fetchUserIfExists(orderModel.getUserId());
        List<OrderItem> orderItems = validateClientAndProductCombination(orderModel);
        AddressEntity addressEntity = getOrCreateShippingDetails(orderModel.getShippingDetails(), user);
        Map<String, Object> inventoryClientErrorMap = checkInventoryAvailable(orderModel);

        Order order = prepareOrder(orderModel, user,addressEntity );
        Map<Long, Map<String, Object>> errorMap = (Map<Long, Map<String, Object>>)inventoryClientErrorMap.get("errorMap");
        orderItems.stream()
                .filter(orderItem -> !errorMap.containsKey(orderItem.getProduct().getId()))
                .forEach(order::addOrderItem);

        try {
            if(!Objects.isNull(order.getOrderItems()) && !CollectionUtils.isEmpty(order.getOrderItems())) {
                orderRepository.save(order);
            }
        } catch (Exception exception) {
            InventoryClientDto inventoryClientDto = (InventoryClientDto) inventoryClientErrorMap.get("inventoryClientDto");
            for (InventoryItemClientDto inventoryItemClientDto : inventoryClientDto.getInventoryItemClientDtos()) {
                if(inventoryItemClientDto.getReservationId() != null) {
                    reservationRepository.deleteById(inventoryItemClientDto.getReservationId());
                }
            }
            // Log the exception (optional)
            // logger.error("Error while saving the Order, reverting back reservation", exception);
            throw new HttpServerErrorException(HttpStatusCode.valueOf(500), "Error while saving the Order, reverting back reservation");
        }
        //map response
        OrderMapper.entityToModel(order,orderModel);
        orderModel.setAdditionalInfo(errorMap);

        return OrderMapper.entityToModel(order,orderModel);
    }

    private User fetchUserIfExists(String userId) {
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new NotFoundException("User does not exist"));
    }

    private Map<String, Object> checkInventoryAvailable(OrderModel orderModel) {
            InventoryClientDto inventoryClientDto = new InventoryClientDto();
            inventoryClientDto.setUserId(orderModel.getUserId());
            inventoryClientDto.setInventoryItemClientDtos(orderModel.getItems().stream()
                    .map(orderItemModel -> InventoryClientMapper.orderItemModelToInventoryRequest(orderItemModel, new InventoryItemClientDto()))
                    .collect(Collectors.toList()));

            InventoryClientDto responseObject = inventoryClient.validateInventories(inventoryClientDto);
            Map<Long, Map<String, Object>> errorMap = responseObject.getInventoryItemClientDtos().stream()
                    .filter(inventoryItemRequestDto -> !inventoryItemRequestDto.isSuccess())
                    .collect(Collectors.toMap(
                            InventoryItemClientDto::getProductId,
                            inventoryItemRequestDto -> {
                                Map<String, Object> inventoryMap = new HashMap<>();
                                inventoryMap.put("message", inventoryItemRequestDto.getMessage());
                                inventoryMap.put("productId", inventoryItemRequestDto.getProductId());
                                inventoryMap.put("reservationId", inventoryItemRequestDto.getReservationId());
                                return inventoryMap;
                            }
                    ));

            Map<String, Object> result = new HashMap<>();
            result.put("inventoryClientDto", responseObject);
            result.put("errorMap", errorMap);
            return result;
        }

    private List<OrderItem> validateClientAndProductCombination(OrderModel orderModel) {
        return orderModel.getItems().stream()
                .map(orderItemModel -> {
                    Client client = clientRepository.findById(orderItemModel.getClientId())
                            .orElseThrow(() -> new NotFoundException("Client does not exist"));
                    Product product = productRepository.findByProductIdAndClientId(orderItemModel.getProductId(), orderItemModel.getClientId())
                            .orElseThrow(() -> new NotFoundException("Combination of Product and Client does not exist"));
                    OrderItem orderItem = new OrderItem();
                    orderItem.setClient(client);
                    orderItem.setProduct(product);
                    OrderItemMapper.itemModelToEntity(orderItemModel, orderItem);
                    return orderItem;
                })
                .collect(Collectors.toList());
    }

    private AddressEntity getOrCreateShippingDetails(ShippingDetailsModel shippingDetailsModel, User user) {
        String normalizedHash = generateAddressHash(
                shippingDetailsModel.getStreet1(), shippingDetailsModel.getStreet2(), shippingDetailsModel.getCity(),
                shippingDetailsModel.getState(), shippingDetailsModel.getPostalCode(), shippingDetailsModel.getCountry()
        );

        return addressRepository.findByAddressHashAndUserId(normalizedHash, user.getId())
                .orElseGet(() -> {
                    AddressEntity newDetails = ShippingAddressMapper.modelToEntity(shippingDetailsModel, new AddressEntity());
                    newDetails.setUser(user);
                    newDetails.setAddressHash(normalizedHash);
                    return addressRepository.save(newDetails);
                });
    }

    private String generateAddressHash(String street1, String street2, String city, String state, String postalCode, String country) {
        String normalizedAddress = normalizeStreet(street1) + "|" +
                normalizeStreet(street2) + "|" +
                city.toLowerCase().trim() + "|" +
                state.toLowerCase().trim() + "|" +
                postalCode.trim() + "|" +
                country.toLowerCase().trim();
        return DigestUtils.md5DigestAsHex(normalizedAddress.getBytes());
    }

    private String normalizeStreet(String street) {
        if (street == null) return "";
        return street.toLowerCase().trim()
                .replace(" street", " st.")
                .replace(" road", " rd.")
                .replace(" avenue", " ave.")
                .replace(" boulevard", " blvd.")
                .replace(" lane", " ln.")
                .replace(" drive", " dr.");
    }

    private static Order prepareOrder(OrderModel orderModel, User user, AddressEntity addressEntity) {
        Order order = new Order();
        order.setUser(user);
        order.setEmail(orderModel.getEmail());
        order.setOrderStatus(OrderStatus.IN_PROGRESS);
        order.setAddressEntity(addressEntity);
        order.setPaymentMode(orderModel.getPaymentMode());
        return order;
    }
}