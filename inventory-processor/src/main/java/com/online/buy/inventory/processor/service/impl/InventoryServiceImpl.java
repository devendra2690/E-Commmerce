package com.online.buy.inventory.processor.service.impl;

import com.online.buy.common.code.entity.Product;
import com.online.buy.common.code.entity.Reservation;
import com.online.buy.common.code.enums.ReservationError;
import com.online.buy.common.code.enums.ReservationStatus;
import com.online.buy.common.code.repository.ProductRepository;
import com.online.buy.common.code.repository.ReservationRepository;
import com.online.buy.inventory.processor.model.OrderItemModel;
import com.online.buy.inventory.processor.model.OrderModel;
import com.online.buy.inventory.processor.service.BatchUpdateRepository;
import com.online.buy.inventory.processor.service.InventoryService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;
    private final ReservationRepository reservationRepository;
    private final BatchUpdateRepository batchUpdateRepository;

    @Transactional
    @Override
    public OrderModel updateInventoryIfAvailable(OrderModel orderModel) {

        for(OrderItemModel orderItemModel : orderModel.getOrderItemModelList()) {

            try {
                // Check if an active reservation already exists
                Optional<Reservation> existingReservation = reservationRepository
                        .findByUserIdAndProductIdAndQuantityAndStatus(
                                orderModel.getUserId(),
                                orderItemModel.getProductId(),
                                orderItemModel.getQuantity(),
                                ReservationStatus.ACTIVE);

                if (existingReservation.isPresent()) {
                    orderItemModel.setSuccess(false);
                    orderItemModel.setMessage(ReservationError.DUPLICATE_RESERVATION.getMessage());
                    //logger.warn("Reservation already exists for user: {}, product: {}", orderModel.getUserId(), orderItemModel.getProductId());
                    continue;
                }

                Optional<Product> fetchedProduct = productRepository.findQuantityByProductId(orderItemModel.getProductId());
                if (fetchedProduct.isEmpty()) {
                    orderItemModel.setSuccess(false);
                    continue;
                }
                Product product = fetchedProduct.get();
                if (product.getQuantity() >= orderItemModel.getQuantity()) {

                    Reservation reservation = new Reservation();
                    reservation.setStatus(ReservationStatus.ACTIVE);
                    reservation.setUserId(orderModel.getUserId());
                    reservation.setProductId(orderItemModel.getProductId());
                    reservation.setQuantity(orderItemModel.getQuantity());

                    reservationRepository.save(reservation);

                    product.setQuantity(product.getQuantity() - orderItemModel.getQuantity());
                    productRepository.save(product);

                    orderItemModel.setSuccess(true);
                    orderItemModel.setReservationId(reservation.getId());
                }else {
                    orderItemModel.setSuccess(false);
                    orderItemModel.setMessage(ReservationError.INSUFFICIENT_STOCK.getMessage());
                }
            }catch (Exception e) {
                orderItemModel.setSuccess(false);
                orderItemModel.setMessage("Error processing reservation");
                //logger.error("Error processing reservation for product: {}", orderItemModel.getProductId(), e);
            }
        }
        return orderModel;
    }

    @Override
    public void updateReservation(List<Long> reservationIds) {
        try {
            batchUpdateRepository.batchUpdateReservations(reservationIds);
        }catch (Exception exception) {
            // TODO: Throwing error won't do much, if it fails we need to send it to DLQ and generate notification
            throw new HttpServerErrorException(HttpStatusCode.valueOf(500), "Batch Updated failed for reservation Ids :"+reservationIds);
        }
    }
}
