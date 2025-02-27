package com.online.buy.order.processor.service.impl;

import com.online.buy.common.code.entity.AddressEntity;
import com.online.buy.common.code.entity.User;
import com.online.buy.common.code.repository.AddressRepository;
import com.online.buy.common.code.repository.ClientRepository;
import com.online.buy.common.code.repository.ProductRepository;
import com.online.buy.common.code.repository.UserRepository;
import com.online.buy.exception.processor.model.NotFoundException;
import com.online.buy.order.processor.client.InventoryClient;
import com.online.buy.order.processor.client.dto.InventoryRequest;
import com.online.buy.order.processor.mapper.InventoryClientMapper;
import com.online.buy.order.processor.mapper.ShippingAddressMapper;
import com.online.buy.order.processor.model.OrderModel;
import com.online.buy.order.processor.model.ShippingDetailsModel;
import com.online.buy.order.processor.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final InventoryClient inventoryClient;

    @Transactional
    @Override
    public void processOrder(OrderModel orderModel) {

        //validate user exits or not
        User user = userRepository.findById(UUID.fromString(orderModel.getUserId())).orElseThrow(() -> new NotFoundException("User does not exist"));

        // Validate client exists and combination or client and productId
        orderModel.getItems().stream().forEach(orderItemModel -> {

            // validate client
            clientRepository.findById(orderItemModel.getClientId()).orElseThrow(() -> new NotFoundException("Client does not exists"));

            // validate client and product combination
            productRepository.findByProductIdAndClientId(orderItemModel.getProductId()
                    , orderItemModel.getClientId()).orElseThrow(() -> new NotFoundException("Combination of Product " + orderItemModel.getProductId() +
                    " and client " + orderItemModel.getClientId() + " does not exist"));
        });

        // validate if address exists for user if not add it
        AddressEntity addressEntity = getOrCreateShippingDetails(orderModel.getShippingDetails(), user);

        //Validate Inventory is out of stock
        List<InventoryRequest> inventoryRequests = orderModel.getItems().stream().map(orderItemModel -> InventoryClientMapper.orderItemModelToInventoryRequest(orderItemModel, new InventoryRequest())).toList();
        Map<Long,String> inventoryValidateResult = inventoryClient.validateInventories(inventoryRequests);

    }


    public AddressEntity getOrCreateShippingDetails(ShippingDetailsModel shippingDetailsModel, User user) {
        String normalizedHash = generateAddressHash(
                shippingDetailsModel.getStreet1(), shippingDetailsModel.getStreet2(), shippingDetailsModel.getCity(),
                shippingDetailsModel.getState(), shippingDetailsModel.getPostalCode(), shippingDetailsModel.getCountry()
        );

        Optional<AddressEntity> existingDetails = addressRepository.findByAddressHashAndUserId(normalizedHash, user.getId());

        return existingDetails.orElseGet(() -> {
            AddressEntity newDetails = ShippingAddressMapper.modelToEntity(shippingDetailsModel, new AddressEntity());
            newDetails.setUser(user);
            newDetails.setAddressHash(normalizedHash);
            return addressRepository.save(newDetails);
        });
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

    private String generateAddressHash(String street1, String street2, String city, String state, String postalCode, String country) {
        String normalizedAddress = normalizeStreet(street1) + "|" +
                normalizeStreet(street2) + "|" +
                city.toLowerCase().trim() + "|" +
                state.toLowerCase().trim() + "|" +
                postalCode.trim() + "|" +
                country.toLowerCase().trim();
        return DigestUtils.md5DigestAsHex(normalizedAddress.getBytes());
    }
}
