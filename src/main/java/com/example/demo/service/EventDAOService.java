package com.example.demo.service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.Exception.HTTPException;
import com.example.demo.model.Events;
import com.example.demo.repository.EventRepository;
import com.opencsv.CSVReader;

@Service
public class EventDAOService {
Logger logger = LoggerFactory.getLogger(EventDAOService.class);
	
	@Autowired
	EventRepository eventsRepository;

	/**
	 * 
	 * @param records
	 * JPA DAO layer to persist the Events of data to the datasource
	 */
	public void saveEvents(List<Events> records)
	{
		try
		{
			eventsRepository.saveAll(records);
			logger.info("Bulk saving of Events records finished");
		}
		catch(Exception e)
		{
			logger.error("Error! There is an error when saving the data to DB");
			throw new HTTPException("There is an error when saving the data to DB, please try after sometime",
					HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	/**
	 * Setting up the CSV reader with reader object and loading the csv file available
	 * in the class path
	 */
	public void readCsvFile() {
		Reader reader = null;
		CSVReader csvReader = null;
		try {
			reader = Files.newBufferedReader(Paths.get("events.csv"));
			csvReader = new CSVReader(reader);
		} catch (IOException e1) {
			logger.error("Error! There is an error with reader. Please check with your csv file");
			throw new HTTPException("There is an error with reader. Please check with your csv file",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}


		try {
			parseCsvFile(csvReader);
		}  catch (Exception e) {
			throw new HTTPException("Error in parsing. Please visit parsing logic to fix it", HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				reader.close();
				csvReader.close();
			} catch (IOException e) {
				logger.error("Error! Something went wrong resource close");
			}
		}
	
	}

	/**
	 * Takes the CsvReader object as argument and fetches each row of records and adds the data to list
	 * calls the saveEvents method to save the records to the datasource.
	 * @param csvReader : handles the reading of data.
	 */
	public void parseCsvFile(CSVReader csvReader) throws Exception
	{
		List<Events> csvRecords = new ArrayList<>();
		try {
			String[] record;
			csvReader.readNext(); //skipping header
			while ((record = csvReader.readNext()) != null) {
				String[] tsv = record[0].split("\\t");
				csvRecords.add(getPageOnSet(tsv));
			}
			saveEvents(csvRecords);
		} catch (IOException ioe) {
			logger.error("Error! There is something wrong in reading the csv file");
			throw new HTTPException("There is something wrong in reading the csv file",
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception validationException) {
			throw new HTTPException("Csv file validation is failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	/**
	 *  Method sets the data of csv to the entity class that can be saved using ORM to
	 *  save the data to configured datasource.
	 * @param tsv array contains the parsed data needs to be set to entity object
	 * @return Page object to add in list of object and save using ORM
	 */
	public Events getPageOnSet(String[] tsv) {
		Events page = new Events();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			page.setUuid(tsv[0]);
			page.setTstamp(tsv[1]);
			page.setSource(tsv[2]);
			Date date = dateFormat.parse(tsv[3]);
			page.setDate(date);
			page.setEvent_action(tsv[4]);
			page.setEvent_category(tsv[5]);
			page.setEvent_action(tsv[6]);
			page.setEvent_label(tsv[7]);
			page.setEvent_type(tsv[8]);
			page.setCreated_at(tsv[9]);
			page.setLast_updated_at(tsv[10]);
			page.setLocation(tsv[11]);
			page.setId(tsv[12]);
		} catch (Exception e) {
			logger.error("Error! something wrong on setting the data.Detailed error message : {}",e.getMessage());
		}
		return page;
	}
	
}
