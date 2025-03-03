package com.online.buy.registration.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@ComponentScan("com.online.buy")
@EnableMethodSecurity
@EnableJpaRepositories("com.online.buy")
@EntityScan("com.online.buy")
public class RegistrationProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegistrationProcessorApplication.class, args);
	}

}
