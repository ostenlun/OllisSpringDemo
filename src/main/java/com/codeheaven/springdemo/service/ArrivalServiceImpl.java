package com.codeheaven.springdemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.codeheaven.springdemo.model.Arrival;
import com.codeheaven.springdemo.repository.ArrivalRepository;

/**
 * ArrivalServiceImpl contains the implementations required in ArrivalService interface.
 * 
 * @author Olli Stenlund
 *
 */

@Service
public class ArrivalServiceImpl implements ArrivalService {

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Override
	public void saveArrival(Arrival arrival) {
		mongoTemplate.save(arrival);
	}
	
	@Override
	public List<Arrival> getHistory() {
		List<Arrival> list = mongoTemplate.findAll(Arrival.class);
		return list;
	}

	@Override
	public void deleteAll() {
		mongoTemplate.dropCollection("TflArrivals");;
	}
}
