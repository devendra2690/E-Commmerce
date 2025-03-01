package com.online.buy.common.code.repository;

import com.online.buy.common.code.entity.Reservation;
import com.online.buy.common.code.enums.ReservationStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    Optional<Reservation> findByUserIdAndProductIdAndQuantityAndStatus(String userId, Long productId, int quantity, ReservationStatus status);
    List<Reservation> findByStatus(ReservationStatus status);
}
