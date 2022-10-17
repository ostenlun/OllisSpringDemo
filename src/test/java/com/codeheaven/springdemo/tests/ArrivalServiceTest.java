package com.codeheaven.springdemo.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.json.JSONException;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.codeheaven.springdemo.model.Arrival;
import com.codeheaven.springdemo.service.ArrivalService;
import com.codeheaven.springdemo.service.ArrivalServiceImpl;

/**
 * Test class for ArrivalServiceImpl class
 * 
 * @author Olli Stenlund
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ArrivalServiceTest {
	@Mock
	MongoTemplate mongoTemplate;
    
	@InjectMocks
	private ArrivalService arrivalService = new ArrivalServiceImpl();

    @MockBean
    private RestTemplate restTemplate;
    
    @Before
	public void init() {
		MockitoAnnotations.openMocks(this);
	}
    
    @Test
    public void testConsumeWebApi() {
	    String actualJson = new String("[{\"$type\":\"Tfl.Api.Presentation.Entities.Prediction, Tfl.Api.Presentation.Entities\",\"id\":\"246812059\",\"operationType\":1,\"vehicleId\":\"YX14RYO\",\"naptanId\":\"490009333W\",\"stationName\":\"Lower Marsh Lane\",\"lineId\":\"k1\",\"lineName\":\"K1\",\"platformName\":\"null\",\"direction\":\"inbound\",\"bearing\":\"246\",\"destinationNaptanId\":\"\",\"destinationName\":\"New Malden\",\"timestamp\":\"2022-10-14T16:25:26.9521123Z\",\"timeToStation\":159,\"currentLocation\":\"\",\"towards\":\"Tolworth\",\"expectedArrival\":\"2022-10-14T16:28:05Z\",\"timeToLive\":\"2022-10-14T16:28:35Z\",\"modeName\":\"bus\",\"timing\":{\"$type\":\"Tfl.Api.Presentation.Entities.PredictionTiming, Tfl.Api.Presentation.Entities\",\"countdownServerAdjustment\":\"00:00:21.8481257\",\"source\":\"2022-10-13T15:23:26.29Z\",\"insert\":\"2022-10-14T16:23:16.289Z\",\"read\":\"2022-10-14T16:23:38.128Z\",\"sent\":\"2022-10-14T16:25:26Z\",\"received\":\"0001-01-01T00:00:00Z\"}}]");
	   
	    when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.any(), 
	    	ArgumentMatchers.<Class<String>>any())).
        	thenReturn(new ResponseEntity<String>(actualJson, HttpStatus.OK));
        
        String resultJson = ((ArrivalServiceImpl) arrivalService).consumeArrivalApi();
        
        try {
			Assertions.assertTrue(JSONCompare.compareJSON(resultJson, actualJson, JSONCompareMode.NON_EXTENSIBLE).passed());
		} catch (JSONException e) {
			e.printStackTrace();
			fail("JSON error: " + e.getMessage());
		}
	}

	@Test
	public void testSaveArrival() {
		Arrival arrival = new Arrival();
		arrivalService.saveArrival(arrival);
	    verify(mongoTemplate, times(1)).save(arrival);
        verifyNoMoreInteractions(mongoTemplate);
	}

	@Test
	public void testGetAllArrivals() {
        when(mongoTemplate.findAll(Arrival.class)).thenReturn(List.of(new Arrival(), new Arrival()));
        assertThat(arrivalService.getAllArrivals()).hasSize(2);
        verify(mongoTemplate, times(1)).findAll(Arrival.class);
        verifyNoMoreInteractions(mongoTemplate);
	}
}
