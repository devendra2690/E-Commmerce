package com.online.buy.payment.processor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ReservationMessageDto {

    @JsonProperty("reservationId")
    List<Long> reservationId;
}
