package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Exception.HTTPException;
import com.example.demo.model.Events;
import com.example.demo.response.ResponseEntity;
import com.example.demo.service.DateVerification;
import com.example.demo.service.EventDAOService;
import com.example.demo.service.EventHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class EventController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	@Autowired
	EventHelper service;
	
	@Autowired
	EventDAOService eventDAOService;
	
	
	@GetMapping("/getTopMostViewdEvents")
	public org.springframework.http.ResponseEntity<Object> getTopMostViewdEvents(@RequestParam(value="nVal") int nVal,@RequestParam(value="startDate",required=false) String startDate,
			@RequestParam(value="endDate",required=false) String endDate,@RequestParam(value="selectedDate",required=false) String selectedDate)
	{
		ResponseEntity responseEntity = null;
		try
		{
			Object object = null;
			if(startDate!=null && !startDate.equals("null") && endDate!=null && !endDate.equals("null"))
			{
				object= service.topMostViewedEvents(nVal, startDate, endDate);
			}
			else if(selectedDate!=null && !selectedDate.equals("null"))
			{
				object= service.topMostViewedEvents(nVal, selectedDate);
			}
			else
			{
				object= service.topMostViewedEvents(nVal);
			}
			responseEntity =ResponseEntity.getResponseEntity(true, object);
		}
		catch(Exception e)
		{
			logger.error("Error occured in api end point /getTopMostViewdEvents ");
			responseEntity = ResponseEntity.getResponseEntity(false, e);
		}
		return new org.springframework.http.ResponseEntity<>(responseEntity,responseEntity.getStatusCode());
	}
	
	@GetMapping("/getTopMostTimeSpentEvents")
	public org.springframework.http.ResponseEntity<Object> getTopMostTimeSpentEvents(@RequestParam(value="nVal") int nVal,@RequestParam(value="startDate",required=false) String startDate,
			@RequestParam(value="endDate",required=false) String endDate,@RequestParam(value="selectedDate",required=false) String selectedDate)
	{
		ResponseEntity responseEntity = null;
		try
		{
			Object object = null;
			if(startDate!=null && !startDate.equals("null") && endDate!=null && !endDate.equals("null"))
			{
				object = service.topMostTimeSpentEvents(nVal, startDate, endDate);
			}
			else if(selectedDate!=null && !selectedDate.equals("null"))
			{
				object = service.topMostTimeSpentEvents(nVal, selectedDate);
			}
			else
			{
				object = service.topMostTimeSpentEvents(nVal);
			}
			responseEntity = ResponseEntity.getResponseEntity(true, object);
		}
		catch(Exception e)
		{
			logger.error("Error occured in api end point /getTopMostTimeSpentEvents ");
			responseEntity = ResponseEntity.getResponseEntity(false, e);
		}
		return new org.springframework.http.ResponseEntity<>(responseEntity,responseEntity.getStatusCode());
	}
	
	@GetMapping("/getTopMostTimeSpentUsers")
	public org.springframework.http.ResponseEntity<Object> getTopMostTimeSpentUsers(@RequestParam(value="nUsers") int nVal,@RequestParam(value="startDate",required=false) String startDate,
			@RequestParam(value="endDate",required=false) String endDate,@RequestParam(value="selectedDate",required=false) String selectedDate)
	{
		ResponseEntity responseEntity = null;
		try
		{
			Object object = null;
			if(startDate!=null && !startDate.equals("null") && endDate!=null && !endDate.equals("null"))
			{
				object = service.topMostTimeSpentUsers(nVal, startDate, endDate);
			}
			else if(selectedDate!=null && !selectedDate.equals("null"))
			{
				object = service.topMostTimeSpentUsers(nVal, selectedDate);
			}
			else
			{
				object = service.topMostTimeSpentUsers(nVal);
			}
			
			responseEntity = ResponseEntity.getResponseEntity(true, object);
		}
		catch(Exception e)
		{
			logger.error("Error occured in api end point /getTopMostTimeSpentUsers ");
			responseEntity = ResponseEntity.getResponseEntity(false, e);
			
		}
		
		return new org.springframework.http.ResponseEntity<>(responseEntity,responseEntity.getStatusCode());
	}
	
	@PostMapping("/oneTimeRecordsSave")
	public org.springframework.http.ResponseEntity<Object> oneTimeRecordsSave()
	{
		ResponseEntity responseEntity = null;
		try
		{
			eventDAOService.readCsvFile();
			responseEntity = ResponseEntity.getResponseEntity(true, "CSV data is parsed and saved to Datasource");
		}
		catch(Exception e)
		{
			logger.error("Error occured in api end point /oneTimeRecordsSave ");
			responseEntity = ResponseEntity.getResponseEntity(false, e);
		}
		return new org.springframework.http.ResponseEntity<>(responseEntity,responseEntity.getStatusCode());
	}
	
	@GetMapping("/reports/events/mostViewed")
	public org.springframework.http.ResponseEntity<Object> mostViewdEvents(@RequestParam(value="nVal") int nVal,@RequestParam(value="startDate",required=false) String startDate,
			@RequestParam(value="endDate",required=false) String endDate,@RequestParam(value="selectedDate",required=false) String selectedDate)
	{
		ResponseEntity responseEntity = null;
		try
		{
			responseEntity =ResponseEntity.getResponseEntity(true, service.getMostViewedEvents(nVal, selectedDate, startDate, endDate));
		}
		catch(Exception e)
		{
			logger.error("Error occured in api end point /reports/Events/mostViewed ");
			responseEntity = ResponseEntity.getResponseEntity(false, e);
		}
		return new org.springframework.http.ResponseEntity<>(responseEntity,responseEntity.getStatusCode());
	}
	
	@GetMapping("/reports/events/mostTimeSpent")
	public org.springframework.http.ResponseEntity<Object> mostTimeSpent(@RequestParam(value="nVal") int nVal,@RequestParam(value="startDate",required=false) String startDate,
			@RequestParam(value="endDate",required=false) String endDate,@RequestParam(value="selectedDate",required=false) String selectedDate)
	{
		ResponseEntity responseEntity = null;
		try
		{
			responseEntity =ResponseEntity.getResponseEntity(true, service.getMostTimeSpentEvents(nVal, selectedDate, startDate, endDate));
		}
		catch(Exception e)
		{
			logger.error("Error occured in api end point /reports/Events/mostTimeSpent ");
			responseEntity = ResponseEntity.getResponseEntity(false, e);
		}
		return new org.springframework.http.ResponseEntity<>(responseEntity,responseEntity.getStatusCode());
	}
	@GetMapping("/reports/users/mostTimeSpent")
	public org.springframework.http.ResponseEntity<Object> mostTimeSpentUser(@RequestParam(value="nUser") int nUser,@RequestParam(value="startDate",required=false) String startDate,
			@RequestParam(value="endDate",required=false) String endDate,@RequestParam(value="selectedDate",required=false) String selectedDate)
	{
		ResponseEntity responseEntity = null;
		try
		{
			responseEntity =ResponseEntity.getResponseEntity(true, pageService.getMostTimeSpentUser(nUser, selectedDate, startDate, endDate));
		}
		catch(Exception e)
		{
			logger.error("Error occured in api end point /reports/users/mostTimeSpent ");
			responseEntity = ResponseEntity.getResponseEntity(false, e);
		}
		return new org.springframework.http.ResponseEntity<>(responseEntity,responseEntity.getStatusCode());
	}
	
	
}
