package com.online.buy.inventory.processor;

import jakarta.persistence.Entity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.online.buy")
@EntityScan("com.online.buy")
@EnableJpaRepositories("com.online.buy")
public class InventoryProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryProcessorApplication.class, args);
	}

}
