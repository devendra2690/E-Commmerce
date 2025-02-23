package com.online.buy.consumer.registration.exception;

import org.springframework.http.HttpStatus;

public class DataNotFoundException extends RuntimeException{

    private HttpStatus httpStatus;
    private String developerMessage;
    private String uiMessage;

    public DataNotFoundException(HttpStatus httpStatus, String developerMessage, String uiMessage) {
       this.httpStatus = httpStatus;
       this.developerMessage = developerMessage;
       this.uiMessage = uiMessage;
    }
}
