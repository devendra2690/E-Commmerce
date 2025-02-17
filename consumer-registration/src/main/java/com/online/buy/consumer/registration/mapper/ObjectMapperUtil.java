package com.online.buy.consumer.registration.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T map(Object source, Class<T> destinationType) {
        return objectMapper.convertValue(source, destinationType);
    }
}
