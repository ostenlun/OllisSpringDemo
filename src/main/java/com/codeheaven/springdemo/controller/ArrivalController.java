package com.codeheaven.springdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.String;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.web.client.RestTemplate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codeheaven.springdemo.model.Arrival;
import com.codeheaven.springdemo.service.ArrivalService;
import com.codeheaven.springdemo.service.ArrivalServiceImpl;

import java.lang.NumberFormatException;

/**
 * ArrivalController handles the REST API calls for retrieving arrivals for a certain bus stop.
 * 
 * @author Olli Stenlund
 *
 */
@RestController
@RequestMapping("/")
public class ArrivalController {
	private static final Logger Log = LoggerFactory.getLogger(ArrivalController.class);

	@Autowired
	private ArrivalService arrivalService;

	@GetMapping("/")
	public String index() {
		return "Please call either /arrivals or /history";
	}
	
	// Retrieves arrivals for a specific bus stop and saves them into database along with a timestamp
	@GetMapping("/arrivals")
	@CrossOrigin()
	public String arrivals() {
		if (arrivalService != null) {
			return arrivalService.getAndStoreArrivals();
		} else {
			return "Error: ArrivalService instance null";
		}
	}
	
	// Finds all arrival data in the database
	@GetMapping("/history")
	@CrossOrigin()
	public String history() {
		if (arrivalService != null) {
			return arrivalService.getHistory();
		} else {
			return "Error: ArrivalService instance null";
		}
	}

	// Delete all arrivals in the database
	@GetMapping("/deleteAll")
	@CrossOrigin()
	public String deleteAll() 
	{
		if (arrivalService != null) {
			return arrivalService.deleteAllArrivals();
		} else {
			return "Error: ArrivalService instance null";
		}
	}
}