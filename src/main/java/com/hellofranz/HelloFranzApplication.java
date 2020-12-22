package com.hellofranz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *     This is the required entrypoint for Spring Boot.
 *
 *     It's what tells Spring to do all of the automatic
 * configuration and dependency injection before anything actually runs.
 */
@SpringBootApplication
public class HelloFranzApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloFranzApplication.class, args);
	}

}
