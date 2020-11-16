package com.example.demo.Exception;

import org.springframework.http.HttpStatus;

public class HTTPException extends RuntimeException{

	private final String errorMessage;
	private final HttpStatus statusCode;
	
	public HTTPException(String errorMessage, HttpStatus statusCode) {
		super();
		this.errorMessage = errorMessage;
		this.statusCode = statusCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}


	public HttpStatus getStatusCode() {
		return statusCode;
	}

	
}
