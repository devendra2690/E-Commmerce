package com.online.buy.order.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.online.buy")
public class OrderProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderProcessorApplication.class, args);
	}

}
