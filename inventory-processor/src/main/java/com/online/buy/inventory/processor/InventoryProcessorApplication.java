package com.online.buy.inventory.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.online.buy")
public class InventoryProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryProcessorApplication.class, args);
	}

}
