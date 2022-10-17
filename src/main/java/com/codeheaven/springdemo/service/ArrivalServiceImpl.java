package com.codeheaven.springdemo.service;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONString;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.codeheaven.springdemo.controller.ArrivalController;
import com.codeheaven.springdemo.model.Arrival;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ArrivalServiceImpl contains the implementations required in ArrivalService interface.
 * 
 * @author Olli Stenlund
 */
@Service
public class ArrivalServiceImpl implements ArrivalService {
	public static final String WEB_API_URL = "https://api.tfl.gov.uk/StopPoint/490009333W/arrivals";
	private static final Logger Log = LoggerFactory.getLogger(ArrivalController.class);
	private RestTemplate restTemplate = new RestTemplate();

    @Autowired
	MongoTemplate mongoTemplate;
	
	// Retrieves arrivals for a specific bus stop and saves them into database along with a timestamp
	@Override
	public String getAndStoreArrivals() {
		String str = "<div>";
		ArrayList<JSONObject> arrivals = new ArrayList<JSONObject>();

		String jsonStr = consumeArrivalApi();
		
		if (jsonStr == null || jsonStr.length() == 0) {
			Log.info("No arrivals found");
			str += "No arrivals found</div>";
			return str;
		}
		
		JSONParser parser = new JSONParser();
		JSONArray jsonArray = new JSONArray();
		
		try {
			jsonArray = (JSONArray) parser.parse(jsonStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
    	Iterator<JSONObject> iterator = jsonArray.iterator();
        
    	while (iterator.hasNext()) {
            arrivals.add(iterator.next());
        }
		
        // Sort arrivals based on arrival time
	    arrivals.sort((o1, o2)->(Integer.valueOf("" + o1.get("timeToStation"))-(Integer.valueOf("" + o2.get("timeToStation")))));

	    iterator = arrivals.iterator();
        String arrivalTimes = "Arrival times: ";
        
        // Save the received arrivals in the database
        while (iterator.hasNext()) {
        	JSONObject jsonObject = iterator.next();
        	Long time = System.currentTimeMillis();
    	    Arrival arrival = new Arrival("" + time, jsonObject.toJSONString());
    		saveArrival(arrival);
    		
    		str += makeArrivalString(time, jsonObject);
    		arrivalTimes += jsonObject.get("timeToStation") + " ";
        }
        
        Log.info(arrivalTimes);
        str += "</div>";
		return str;
	}

	public String consumeArrivalApi() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity<String> entity = new HttpEntity<String>(headers);
	    String strTflResponse = "";
	    
	    try {
	    	strTflResponse = restTemplate.exchange(WEB_API_URL, HttpMethod.GET, entity, String.class).getBody();
	    } catch (RestClientException e) {
	    	e.printStackTrace();
	    	Log.error("Error consuming API: " + e.getMessage());
	    	return "";
	    }
	    
		JSONParser parser = new JSONParser();
		String retStr = "";		
		
	    try (Reader reader = new StringReader(strTflResponse)) {
	    	retStr = parser.parse(reader).toString();
	    } catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    
        return retStr;
	}
	    	
	@Override
	public void saveArrival(Arrival arrival) {
		mongoTemplate.save(arrival);
	}
	
	// Finds all arrival data in the database
	@Override
	public String getHistory() {
		String str = "<div>";
		
		List<Arrival> arrivals = getAllArrivals();
		
		if (arrivals.size() == 0) {
			Log.info("No history found");
			str += "No arrivals found</div>";
			return str;
		}
				
		JSONParser parser = new JSONParser();
		Log.info("Arrivals found: " + arrivals.size());

		// Go through arrivals and present them in string form
		for (int i = 0; i < arrivals.size(); i++) {
			Arrival arrival = arrivals.get(i);
			
			if (arrival == null) {
				continue;
			}
			
			try (Reader reader = new StringReader(arrival.getArrival())) {
            	JSONObject jsonObject = (JSONObject) parser.parse(reader);
        		long time = Long.valueOf(arrival.getTime());
            	str += makeArrivalString(time, jsonObject);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return str + "</div>";
	}
	
	@Override
	public List<Arrival> getAllArrivals() {
		List<Arrival> list = mongoTemplate.findAll(Arrival.class);
		return list;
	}

	@Override
	public String deleteAllArrivals() {
		mongoTemplate.dropCollection("TflArrivals");
		return "<div>Arrivals deleted</div>";
	}
	
	// Present the arrival in string form
	@Override
	public String makeArrivalString(long time, JSONObject jsonObject) {
		String outputStr = "";
		String strDestination = (String) jsonObject.get("destinationName");
		String strBearing = (String) jsonObject.get("bearing");
		long nTimeToStation = (long) jsonObject.get("timeToStation");
	    long minsToStation = (long) (nTimeToStation / 60);
	
		Date date = new Date(time);
	
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    String strTime = dt.format(date);
	    
		outputStr += "<p>" + strTime + "     bus " + strBearing + " " + strDestination + " " + minsToStation + " mins</p>";
		return outputStr;
	}
}
