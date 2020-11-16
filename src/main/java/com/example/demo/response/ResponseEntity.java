package com.example.demo.response;

import org.springframework.http.HttpStatus;

import com.example.demo.Exception.HTTPException;

public class ResponseEntity {

	
	private boolean status;
	private Object successObject;
	private HttpStatus statusCode;
	private String errorDescription;
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public Object getSuccessObject() {
		return successObject;
	}
	public void setSuccessObject(Object successObject) {
		this.successObject = successObject;
	}
	
	

	public HttpStatus getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	
	public static ResponseEntity getResponseEntity(boolean status, Object object)
	{
		ResponseEntity responseEntity = new ResponseEntity();
		responseEntity.setStatus(status);
		if(object instanceof Exception || object instanceof HTTPException)
		{
			String errorMessage = "";
			HttpStatus statusCode = null;
			if(object instanceof HTTPException)
			{
				errorMessage = ((HTTPException) object).getErrorMessage();
				statusCode = ((HTTPException) object).getStatusCode();
			}
			else
			{
				errorMessage = "Something looks wrong. Please retry after sometime";
				statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
			}
			responseEntity.setErrorDescription(errorMessage);
			responseEntity.setStatusCode(statusCode);
		}
		else
		{
			responseEntity.setStatusCode(HttpStatus.OK);
			responseEntity.setSuccessObject(object);
		}
		return responseEntity;
	}
	
}

