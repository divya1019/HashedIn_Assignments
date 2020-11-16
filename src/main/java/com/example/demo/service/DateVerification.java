package com.example.demo.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.example.demo.Exception.HTTPException;

public class DateVerification {
static Logger logger = LoggerFactory.getLogger(DateVerification.class);

	
	public static void verifyDateFormat(String date)
	{
		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try
		{
			LocalDate.parse(date,dateTimeFormat);
		}
		catch(DateTimeParseException pe)
		{
			logger.error("Error! Please enter proper date/ provide the date in yyyy-MM-dd format");
			throw new HTTPException("Please enter proper date/ provide the date in yyyy-MM-dd format", HttpStatus.BAD_REQUEST);
		}
	}
}
