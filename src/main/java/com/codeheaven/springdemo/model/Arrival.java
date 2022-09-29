package com.codeheaven.springdemo.model;

import java.lang.String;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * Arrival class holds the necessary information about Tfl bus arrival stored in the database.
 * 
 * @author Olli Stenlund
 *
 */
@Document("TflArrivals")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Arrival {

	@Id
	private String strId;
	private String strTime;

	private String strArrival;
	
	public Arrival() {
		super();
	}

	public Arrival(String time, String arrival) {
		super();
		strTime = time;
		strArrival = arrival;
	}

	public String getTime() {
		return strTime;
	}

	public void setTime(String strTime) {
		this.strTime = strTime;
	}

	public String getArrival() {
		return strArrival;
	}

	public void setArrival(String strArrival) {
		this.strArrival = strArrival;
	}

	@Override
	public String toString() {
    	return "Arrival{" +
				"strTime = '" + strTime + '\'' +
				'}';
	}
}