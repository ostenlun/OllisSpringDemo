package com.codeheaven.springdemo.service;

import java.util.List;

import com.codeheaven.springdemo.model.Arrival;

/**
 * ArrivalService interface is implemented for services related to Tfl arrivals in a bus stop.
 * 
 * @author Olli Stenlund
 *
 */
public interface ArrivalService {
	public void saveArrival(Arrival arrival);
	public List<Arrival> getHistory();
	public void deleteAll();
}
