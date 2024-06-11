package com.mateusz.rest.webservices.restful_web_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class RestfulWebServicesApplication {

	public static void main(String[] args) {
		// Dotenv dotenv = Dotenv.load();
		// dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));

		SpringApplication.run(RestfulWebServicesApplication.class, args);
	}

}
