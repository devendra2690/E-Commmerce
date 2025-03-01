package com.online.buy.order.processor.service;

import java.util.List;

public interface ReservationService {
    void rollbackReservation(List<Long> reservationIds);
}
