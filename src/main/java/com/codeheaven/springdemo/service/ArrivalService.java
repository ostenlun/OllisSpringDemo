package com.codeheaven.springdemo.service;

import java.util.List;

import org.json.simple.JSONObject;

import com.codeheaven.springdemo.model.Arrival;

/**
 * ArrivalService interface is implemented for services related to Tfl arrivals in a bus stop.
 * 
 * @author Olli Stenlund
 *
 */
public interface ArrivalService {
	public String getAndStoreArrivals();
	public String getHistory(); 
	public void saveArrival(Arrival arrival);
	public List<Arrival> getAllArrivals();
	public String deleteAllArrivals();
	public String makeArrivalString(long time, JSONObject jsonObject);
}
