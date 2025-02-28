package com.online.buy.common.code.enums;

public enum ReservationError {

    INSUFFICIENT_STOCK(ErrorMessages.INSUFFICIENT_STOCK),
    DUPLICATE_RESERVATION(ErrorMessages.DUPLICATE_RESERVATION);
    // More enum values...

    private final String message;

    ReservationError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
