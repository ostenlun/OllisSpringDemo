package com.codeheaven.springdemo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.codeheaven.springdemo.model.Arrival;

/**
 * Simple ArrivalRepository class implementation
 * 
 * @author Olli Stenlund
 *
 */
public interface ArrivalRepository extends MongoRepository<Arrival, String> {
    
}
