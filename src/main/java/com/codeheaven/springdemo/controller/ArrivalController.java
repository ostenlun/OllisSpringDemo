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
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity <String> entity = new HttpEntity<String>(headers);
	      
	    String strTflResponse = restTemplate.exchange("https://api.tfl.gov.uk/StopPoint/490009333W/arrivals", HttpMethod.GET, entity, String.class).getBody();
		String str = "<div>";
		
		JSONParser parser = new JSONParser();
		ArrayList<JSONObject> arrivals = new ArrayList<JSONObject>();
				
        try (Reader reader = new StringReader(strTflResponse)) {
        	JSONArray jsonArray = (JSONArray) parser.parse(reader);
            Iterator<JSONObject> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                arrivals.add(iterator.next());
            }
	    } catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}   
	    
        // Sort arrivals based on arrival time
	    arrivals.sort((o1, o2)->(Integer.valueOf("" + o1.get("timeToStation"))-(Integer.valueOf("" + o2.get("timeToStation")))));

	    Iterator<JSONObject> iterator = arrivals.iterator();
        String arrivalTimes = "Arrival times: ";
        
        // Save the received arrivals in the database
        while (iterator.hasNext()) {
        	JSONObject jsonObject = iterator.next();
        	Long time = System.currentTimeMillis();
    	    Arrival arrival = new Arrival("" + time, jsonObject.toJSONString());
    		arrivalService.saveArrival(arrival);
    		
    		str += makeArrivalString(time, jsonObject);
    		arrivalTimes += jsonObject.get("timeToStation") + " ";
        }
        
        System.out.println(arrivalTimes);
        str += "</div>";
		return str;
	}
	
	// Finds all arrival data in the database
	@GetMapping("/history")
	@CrossOrigin()
	public String history() {
		String str = "<div>";
		
		List<Arrival> arrivals = arrivalService.getHistory();
		
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
			
			Log.info("Arrival found: " + arrival.toString());
						
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

	// Delete all arrivals in the database
	@GetMapping("/deleteAll")
	@CrossOrigin()
	public String deleteAll() 
	{
		arrivalService.deleteAll();
		Log.info("Arrivals deleted");
		return "<div>Arrivals deleted</div>";
	}
	
	// Present the arrival in string form
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