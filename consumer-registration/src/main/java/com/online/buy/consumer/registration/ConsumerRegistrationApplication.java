package com.online.buy.consumer.registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ConsumerRegistrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerRegistrationApplication.class, args);
	}

}
