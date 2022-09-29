package com.codeheaven.OllisSpringDemo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.codeheaven.springdemo.controller.ArrivalController;

/**
 * Olli's Spring demo test class
 * 
 * @author Olli Stenlund
 *
 */
@SpringBootTest
class OllisSpringDemoApplicationTests {

	@Autowired
	private ArrivalController arrivalController;

	@Test
	public void contextLoads() throws Exception {
		assertThat(arrivalController).isNotNull();
	}
}
