package com.online.buy.order.processor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "com.buy.online")
public class ApplicationUrl {
    private String inventoryValidate;
}
