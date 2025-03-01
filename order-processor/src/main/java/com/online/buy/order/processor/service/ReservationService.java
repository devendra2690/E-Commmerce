package com.online.buy.order.processor.service;

import com.online.buy.common.code.entity.Reservation;

import java.util.List;

public interface ReservationService {
    void rollbackReservation(List<Long> reservationIds);
    List<Reservation> findActiveReservation();
    void updateReservation(Reservation reservation);
}
