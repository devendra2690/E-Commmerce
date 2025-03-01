package com.online.buy.order.processor.service.impl;

import com.online.buy.common.code.repository.ReservationRepository;
import com.online.buy.order.processor.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Override
    public void rollbackReservation(List<Long> reservationIds) {
        for(Long ids: reservationIds) {
            reservationRepository.deleteById(ids);
        }
    }
}
