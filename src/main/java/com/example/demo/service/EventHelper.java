package com.example.demo.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.clapp.exception.HTTPException;
import com.clapp.pojo.Events;
import com.clapp.utility.DateVerification;
import com.example.demo.Exception.HTTPException;
import com.example.demo.model.Events;
import com.example.demo.repository.EventRepository;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
@Service
public class EventHelper {
	
	static Logger logger = LoggerFactory.getLogger(EventHelper.class);
	private static List<Events> csvRecords = new ArrayList<>();
	
	@Autowired
	private EventRepository eventRepo;
	public List<Events> readExcel() {
	CSVReader csvReader = null;
		try {
			csvReader = new CSVReader(new FileReader(
					"/home/hasher/Documents/workspace-spring-tool-suite-4-4.8.1.RELEASE/HUExAssignment/src/main/resources/static/events.csv"));
		} catch (FileNotFoundException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CsvToBean<Events> csvToBean = new CsvToBean<>();
		List<String[]> reader = new ArrayList<>();
		try {
			reader = csvReader.readAll();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//remove the column names
		reader.remove(0);
		List<Events> events = new ArrayList<>();
		Iterator<String[]> iterator = reader.iterator();
		while (iterator.hasNext()) {
			String[] record = iterator.next();

			Events e = new Events();
			e.setUuid(record[0]);
			e.setTstamp(Timestamp.valueOf(record[1]));
			e.setSource(record[2]);
			e.setDate(Date.valueOf(record[3]));
			e.setEvent_type(record[4]);
			e.setEvent_category(record[5]);
			e.setEvent_action(record[6]);
			e.setEvent_label(record[7]);
			e.setEvent_value(record[8] == null? 0 : Integer.parseInt(record[8]));
			e.setCreated_at(Timestamp.valueOf(record[9]));
			e.setLast_updated_at(Timestamp.valueOf(record[10]));
			e.setLocation(record[11]);
			e.setId(Integer.parseInt(record[12]));
			events.add(e);
			eventRepo.save(e);
		}
	
		return events;

	}
	
	public List<String> topMostTimeSpentUsers(int noOfUsers, String date) {
		List<String> requiredTopMostSpentUsers = new ArrayList<>();
		try {
			if (noOfUsers <= 0) {
				logger.error("Error! n value is invalid for tom most time spent users report");
				throw new HTTPException("Enter proper n value", HttpStatus.BAD_REQUEST);
			}
			
			DateVerification.verifyDateFormat(date);
			
			
			
			HashMap<String, Long> data = (HashMap<String, Long>) csvRecords.stream()
					.filter(val -> dateFilter(val, date))
					.collect(Collectors.groupingBy(Events::getUuid, Collectors.summingLong(
							page -> page.getEvent_type() != null ? Long.parseLong(page.getEvent_type()) : 0)));

			LinkedHashMap<String, Long> sorteddata = sortMapValuesByDesc(data);

			ArrayList<String> topMostSpentUsers = new ArrayList<>(sorteddata.keySet());
			if (topMostSpentUsers.size() >= noOfUsers) {
				requiredTopMostSpentUsers = topMostSpentUsers.subList(0, noOfUsers);
			} else {
				requiredTopMostSpentUsers = topMostSpentUsers;
			}
		} 
		catch(HTTPException e)
		{
			throw e;
		}
		catch (Exception e) {
			logger.error("Error occured in getting most time spent users report, error details : {}",e.getMessage());
		}

		return requiredTopMostSpentUsers;
	}
	
	public boolean dateFilter(Events page, String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (page.getDate() == null || page.getDate().equals("NULL")) {
			return false;
		}
		
		Date pageCreatedDate = null;
		try {
			pageCreatedDate = dateFormat.parse(page.getDate());
		} catch (ParseException e) {
			logger.error("Error! Invalid date format in csv data, contact admin to cleanup data");
			throw new HTTPException("Invalid date format in csv data, contact admin to cleanup data", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Date dateVal = null;
		try {
			dateVal = (Date) dateFormat.parse(date);
		} catch (ParseException e) {
			logger.error("Error! Invalid date format, provide in yyyy-MM-dd format");
			throw new HTTPException("Invalid date format, provide in yyyy-MM-dd format", HttpStatus.BAD_REQUEST);
		}
		
		if (pageCreatedDate.equals(dateVal)) {
			return true;
		} else {
			return false;
		}

		
	}
	
	public static LinkedHashMap<String, Long> sortMapValuesByDesc(HashMap<String, Long> map) {
		LinkedHashMap<String, Long> sortedMap = new LinkedHashMap<>();
		map.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
		return sortedMap;
	}
	
	public static boolean mostViewedFilter(Events page) {
		if (page.getEvent_action() != null && page.getEvent_action().equals("view")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static Object topMostViewedEvents(int nVal, String startDate, String endDate) {
		List<String> requiredTopViewedEvents = new ArrayList<>();
		try {
			if (nVal <= 0) {
				logger.error("noOfEvents is invalid for the most viewed Events report");
				throw new HTTPException("Enter proper n value", HttpStatus.BAD_REQUEST);
			}
			
			DateVerification.verifyDateFormat(startDate);
			DateVerification.verifyDateFormat(endDate);
			
			HashMap<String, Long> data = (HashMap<String, Long>) csvRecords.stream()
					.filter(val -> mostViewedFilter(val)).filter(val -> dateRangeFilter(val, startDate, endDate))
					.collect(Collectors.groupingBy(Events::getEvent_label, Collectors.counting()));

			LinkedHashMap<String, Long> sorteddata = sortMapValuesByDesc(data);

			ArrayList<String> topViewedEvents = new ArrayList<>(sorteddata.keySet());
			if (topViewedEvents.size() >= nVal) {
				requiredTopViewedEvents = topViewedEvents.subList(0, nVal);
			} else {
				requiredTopViewedEvents = topViewedEvents.subList(0, topViewedEvents.size());
			}

		} 
		catch(HTTPException e)
		{
			throw e;
		}
		catch (Exception e) {
			logger.error("Error occured in getting most viewed Events report, error details : {}",e.getMessage());
		}

		return requiredTopViewedEvents;
	}
	
	public boolean dateRangeFilter(Events page, String fromDate, String toDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (page.getDate() == null || page.getDate().equals("NULL")) {
				return false;
			}
			Date pageCreatedDate = dateFormat.parse(page.getDate());
			Date startDate = (Date) dateFormat.parse(fromDate);
			Date endDate = (Date) dateFormat.parse(toDate);
			if (pageCreatedDate.after(startDate) && pageCreatedDate.before(endDate)) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			logger.error("Error! there is an error in parsing the provided date");
		}
		return false;
	}

	public  Object topMostViewedEvents(int nVal, String selectedDate) {
		List<String> requiredTopMostSpentUsers = new ArrayList<>();
		try {
			if (nVal <= 0) {
				logger.error("Error! n value is invalid for tom most time spent users report");
				throw new HTTPException("Enter proper n value", HttpStatus.BAD_REQUEST);
			}
			
			DateVerification.verifyDateFormat(selectedDate);
			
			
			
			HashMap<String, Long> data = (HashMap<String, Long>) csvRecords.stream()
					.filter(val -> dateFilter(val, selectedDate))
					.collect(Collectors.groupingBy(Events::getUuid, Collectors.summingLong(
							page -> page.getEvent_type() != null ? Long.parseLong(page.getEvent_type()) : 0)));

			LinkedHashMap<String, Long> sorteddata = sortMapValuesByDesc(data);

			ArrayList<String> topMostSpentUsers = new ArrayList<>(sorteddata.keySet());
			if (topMostSpentUsers.size() >= nVal) {
				requiredTopMostSpentUsers = topMostSpentUsers.subList(0, nVal);
			} else {
				requiredTopMostSpentUsers = topMostSpentUsers;
			}
		} 
		catch(HTTPException e)
		{
			throw e;
		}
		catch (Exception e) {
			logger.error("Error occured in getting most time spent users report, error details : {}",e.getMessage());
		}

		return requiredTopMostSpentUsers;
	}

	public Object topMostViewedEvents(int nVal) {
		List<String> requiredTopMostSpentUsers = new ArrayList<>();
		try {
			if (nVal <= 0) {
				logger.error("Error! n value is invalid for tom most time spent users report");
				throw new HTTPException("Enter proper n value", HttpStatus.BAD_REQUEST);
			}
			HashMap<String, Long> data = (HashMap<String, Long>) csvRecords.stream()
					.collect(Collectors.groupingBy(Events::getUuid, Collectors.summingLong(
							page -> page.getEvent_type() != null ? Long.parseLong(page.getEvent_type()) : 0)));

			LinkedHashMap<String, Long> sorteddata = sortMapValuesByDesc(data);

			ArrayList<String> topMostSpentUsers = new ArrayList<>(sorteddata.keySet());
			if (topMostSpentUsers.size() >= nVal) {
				requiredTopMostSpentUsers = topMostSpentUsers.subList(0, nVal);
			} else {
				requiredTopMostSpentUsers = topMostSpentUsers;
			}

		} 
		catch(HTTPException e)
		{
			throw e;
		}
		catch (Exception e) {
			logger.error("Error occured in getting most time spent users report, error details : {}",e.getMessage());
		}

		return requiredTopMostSpentUsers;
	}
	
	public List<String> topMostTimeSpentEvents(int noOfEvents) {
		List<String> requiredTopTimeSpentEvents = new ArrayList<>();
		try {
			if (noOfEvents <= 0) {
				logger.error("noOfEvents is invalid for the most time spent Events report");
				throw new HTTPException("Enter proper n value", HttpStatus.BAD_REQUEST);
			}

			HashMap<String, Long> data = (HashMap<String, Long>) csvRecords.stream()
					.collect(Collectors.groupingBy(Events::getEventLabel, Collectors.summingLong(
							page -> page.getEvent_value() != null ? Long.parseLong(page.getEvent_value()) : 0)));

			LinkedHashMap<String, Long> sorteddata = sortMapValuesByDesc(data);

			ArrayList<String> topTimeSpentEvents = new ArrayList<>(sorteddata.keySet());
			if (topTimeSpentEvents.size() >= noOfEvents) {
				requiredTopTimeSpentEvents = topTimeSpentEvents.subList(0, noOfEvents);
			} else {
				requiredTopTimeSpentEvents = topTimeSpentEvents;
			}

		} 
		catch(HTTPException e)
		{
			throw e;
		}
		catch (Exception e) {
			logger.error("Error occured in getting most time spent Events report, error details : {}",e.getMessage());
		}

		return requiredTopTimeSpentEvents;
	}

	public List<String> topMostTimeSpentEvents(int noOfEvents, String date) {
		List<String> requiredTopTimeSpentEvents = new ArrayList<>();
		try {
			if (noOfEvents <= 0) {
				logger.error("noOfEvents is invalid for the most time spent Events report");
				throw new HTTPException("Enter proper n value", HttpStatus.BAD_REQUEST);
			}
			
			DateVerification.verifyDateFormat(date);
			
			HashMap<String, Long> data = (HashMap<String, Long>) csvRecords.stream()
					.filter(val -> dateFilter(val, date))
					.collect(Collectors.groupingBy(Events::getEventLabel, Collectors.summingLong(
							page -> page.getEvent_value() != null ? Long.parseLong(page.getEvent_value()) : 0)));

			LinkedHashMap<String, Long> sorteddata = sortMapValuesByDesc(data);

			ArrayList<String> topTimeSpentEvents = new ArrayList<>(sorteddata.keySet());
			if (topTimeSpentEvents.size() >= noOfEvents) {
				requiredTopTimeSpentEvents = topTimeSpentEvents.subList(0, noOfEvents);
			} else {
				requiredTopTimeSpentEvents = topTimeSpentEvents;
			}
		} 
		catch(HTTPException e)
		{
			throw e;
		}
		catch (Exception e) {
			logger.error("Error occured in getting most time spent Events reports, error details : {}",e.getMessage());
		}

		return requiredTopTimeSpentEvents;
	}

	public List<String> topMostTimeSpentEvents(int noOfEvents, String startDate, String endDate) {
		List<String> requiredTopTimeSpentEvents = new ArrayList<>();
		try {
			if (noOfEvents <= 0) {
				logger.error("noOfEvents is invalid for the most time spent Events report");
				throw new HTTPException("Enter proper n value", HttpStatus.BAD_REQUEST);
			}
			
			DateVerification.verifyDateFormat(startDate);
			DateVerification.verifyDateFormat(endDate);
			
			HashMap<String, Long> data = (HashMap<String, Long>) csvRecords.stream()
					.filter(val -> dateRangeFilter(val, startDate, endDate))
					.collect(Collectors.groupingBy(Events::getEvent_label, Collectors.summingLong(
							page -> page.getEvent_label() != null ? Long.parseLong(page.getEvent_label()) : 0)));

			LinkedHashMap<String, Long> sorteddata = sortMapValuesByDesc(data);

			ArrayList<String> topTimeSpentEvents = new ArrayList<>(sorteddata.keySet());
			if (topTimeSpentEvents.size() >= noOfEvents) {
				requiredTopTimeSpentEvents = topTimeSpentEvents.subList(0, noOfEvents);
			} else {
				requiredTopTimeSpentEvents = topTimeSpentEvents;
			}

		} 
		catch(HTTPException e)
		{
			throw e;
		}
		catch (Exception e) {
			logger.error("Error occured in getting most time spent Events reports, error details : {}",e.getMessage());
		}

		return requiredTopTimeSpentEvents;
	}

	public List<String> topMostTimeSpentUsers(int noOfUsers) {
		List<String> requiredTopMostSpentUsers = new ArrayList<>();
		try {
			if (noOfUsers <= 0) {
				logger.error("Error! n value is invalid for tom most time spent users report");
				throw new HTTPException("Enter proper n value", HttpStatus.BAD_REQUEST);
			}
			HashMap<String, Long> data = (HashMap<String, Long>) csvRecords.stream()
					.collect(Collectors.groupingBy(Events::getUuid, Collectors.summingLong(
							page -> page.getEvent_type() != null ? Long.parseLong(page.getEvent_type()) : 0)));

			LinkedHashMap<String, Long> sorteddata = sortMapValuesByDesc(data);

			ArrayList<String> topMostSpentUsers = new ArrayList<>(sorteddata.keySet());
			if (topMostSpentUsers.size() >= noOfUsers) {
				requiredTopMostSpentUsers = topMostSpentUsers.subList(0, noOfUsers);
			} else {
				requiredTopMostSpentUsers = topMostSpentUsers;
			}

		} 
		catch(HTTPException e)
		{
			throw e;
		}
		catch (Exception e) {
			logger.error("Error occured in getting most time spent users report, error details : {}",e.getMessage());
		}

		return requiredTopMostSpentUsers;
	}
	
	public List<String> topMostTimeSpentUsers(int noOfUsers) {
		List<String> requiredTopMostSpentUsers = new ArrayList<>();
		try {
			if (noOfUsers <= 0) {
				logger.error("Error! n value is invalid for tom most time spent users report");
				throw new HTTPException("Enter proper n value", HttpStatus.BAD_REQUEST);
			}
			HashMap<String, Long> data = (HashMap<String, Long>) csvRecords.stream()
					.collect(Collectors.groupingBy(Events::getUuid, Collectors.summingLong(
							page -> page.getEvent_value() != null ? Long.parseLong(page.getEvent_value()) : 0)));

			LinkedHashMap<String, Long> sorteddata = sortMapValuesByDesc(data);

			ArrayList<String> topMostSpentUsers = new ArrayList<>(sorteddata.keySet());
			if (topMostSpentUsers.size() >= noOfUsers) {
				requiredTopMostSpentUsers = topMostSpentUsers.subList(0, noOfUsers);
			} else {
				requiredTopMostSpentUsers = topMostSpentUsers;
			}

		} 
		catch(HTTPException e)
		{
			throw e;
		}
		catch (Exception e) {
			logger.error("Error occured in getting most time spent users report, error details : {}",e.getMessage());
		}

		return requiredTopMostSpentUsers;
	}

	
	public List<String> topMostTimeSpentUsers1(int noOfUsers, String date) {
		List<String> requiredTopMostSpentUsers = new ArrayList<>();
		try {
			if (noOfUsers <= 0) {
				logger.error("Error! n value is invalid for tom most time spent users report");
				throw new HTTPException("Enter proper n value", HttpStatus.BAD_REQUEST);
			}
			
			DateVerification.verifyDateFormat(date);
			
			
			
			HashMap<String, Long> data = (HashMap<String, Long>) csvRecords.stream()
					.filter(val -> dateFilter(val, date))
					.collect(Collectors.groupingBy(Events::getUuid, Collectors.summingLong(
							page -> page.getEvent_value() != null ? Long.parseLong(page.getEvent_value()) : 0)));

			LinkedHashMap<String, Long> sorteddata = sortMapValuesByDesc(data);

			ArrayList<String> topMostSpentUsers = new ArrayList<>(sorteddata.keySet());
			if (topMostSpentUsers.size() >= noOfUsers) {
				requiredTopMostSpentUsers = topMostSpentUsers.subList(0, noOfUsers);
			} else {
				requiredTopMostSpentUsers = topMostSpentUsers;
			}
		} 
		catch(HTTPException e)
		{
			throw e;
		}
		catch (Exception e) {
			logger.error("Error occured in getting most time spent users report, error details : {}",e.getMessage());
		}

		return requiredTopMostSpentUsers;
	}

	public List<String> topMostTimeSpentUsers(int noOfUsers, String startDate, String endDate) {
		List<String> requiredTopMostSpentUsers = new ArrayList<>();

		try {
			if (noOfUsers <= 0) {
				logger.error("Error! n value is invalid for tom most time spent users report");
				throw new HTTPException("Enter proper n value", HttpStatus.BAD_REQUEST);
			}
			
			DateVerification.verifyDateFormat(startDate);
			DateVerification.verifyDateFormat(endDate);
			
			HashMap<String, Long> data = (HashMap<String, Long>) csvRecords.stream()
					.filter(val -> dateRangeFilter(val, startDate, endDate))
					.collect(Collectors.groupingBy(Events::getUuid, Collectors.summingLong(
							page -> page.getEvent_value() != null ? Long.parseLong(page.getEvent_value()) : 0)));

			LinkedHashMap<String, Long> sorteddata = sortMapValuesByDesc(data);

			ArrayList<String> topMostSpentUsers = new ArrayList<>(sorteddata.keySet());
			if (topMostSpentUsers.size() >= noOfUsers) {
				requiredTopMostSpentUsers = topMostSpentUsers.subList(0, noOfUsers);
			} else {
				requiredTopMostSpentUsers = topMostSpentUsers;
			}
		} 
		catch(HTTPException e)
		{
			throw e;
		}
		catch (Exception e) {
			logger.error("Error occured in getting most time spent users report, error details : {}",e.getMessage());
		}

		return requiredTopMostSpentUsers;
	}


	
}
