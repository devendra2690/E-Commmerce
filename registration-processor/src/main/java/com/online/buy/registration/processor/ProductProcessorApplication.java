package com.online.buy.registration.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.online.buy")
public class ProductProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductProcessorApplication.class, args);
	}

}
