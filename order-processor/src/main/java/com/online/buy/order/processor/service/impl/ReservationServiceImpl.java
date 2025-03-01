package com.online.buy.order.processor.service.impl;

import com.online.buy.common.code.entity.Reservation;
import com.online.buy.common.code.enums.ReservationStatus;
import com.online.buy.common.code.repository.ReservationRepository;
import com.online.buy.order.processor.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
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

    @Override
    public List<Reservation> findActiveReservation() {
        List<Reservation> reservations = reservationRepository.findByStatus(ReservationStatus.ACTIVE);
        return  reservations.stream().filter(reservation -> Duration.between(reservation.getCreatedAt()
                , LocalDateTime.now()).getSeconds() >= 30).toList();
    }

    @Override
    public void updateReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }
}
