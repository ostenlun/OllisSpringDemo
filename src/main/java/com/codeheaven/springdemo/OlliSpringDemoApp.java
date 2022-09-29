package com.codeheaven.springdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

import com.codeheaven.springdemo.model.Arrival;
import com.codeheaven.springdemo.repository.ArrivalRepository;

import java.lang.String;

/**
 * Olli's simple Spring demo app main class
 * 
 * @author Olli Stenlund
 */
@SpringBootApplication
@EnableMongoRepositories
public class OlliSpringDemoApp implements CommandLineRunner {
    
	@Autowired
    ArrivalRepository arrivalRepository;
	
	private static final Logger log = LoggerFactory.getLogger(OlliSpringDemoApp.class);

	public static void main(String[] args) {
		log.info("Main called");
		SpringApplication.run(OlliSpringDemoApp.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
	}
}
