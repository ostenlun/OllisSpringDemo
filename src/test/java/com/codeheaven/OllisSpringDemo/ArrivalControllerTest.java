package com.codeheaven.OllisSpringDemo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Simple test class for ArrivalController
 * 
 * @author Olli Stenlund
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ArrivalControllerTest {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	@Test
	void historyShouldReturnDefaultMessage() throws Exception {
		assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/history",
			String.class)).contains("<div>");
	}

	@Test
	public void arrivalsShouldReturnDefaultMessage() throws Exception {
		assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/arrivals",
			String.class)).contains("<div>");
	}
}

