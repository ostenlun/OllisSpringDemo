package com.codeheaven.springdemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Olli's simple Spring demo app main class
 * 
 * @author Olli Stenlund
 */
@SpringBootApplication
@EnableMongoRepositories
public class OlliSpringDemoApp implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(OlliSpringDemoApp.class, args);
	}
	
	
	@Override
	public void run(String... args) throws Exception {
	}
}
