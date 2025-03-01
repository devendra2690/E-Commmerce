package com.online.buy.order.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan("com.online.buy")
@EntityScan("com.online.buy")
@EnableJpaRepositories("com.online.buy")
@EnableScheduling
public class OrderProcessorApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrderProcessorApplication.class, args);
	}

}
