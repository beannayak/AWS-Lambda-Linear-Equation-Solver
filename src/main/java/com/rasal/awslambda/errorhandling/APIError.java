package com.rasal.awslambda.errorhandling;

import java.util.List;

public class APIError {
	private int errorCode;
	private String message;
	private List<ErrorDetail> errorDetails;
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public List<ErrorDetail> getErrorDetails() {
		return errorDetails;
	}
	
	public void setErrorDetails(List<ErrorDetail> errorDetails) {
		this.errorDetails = errorDetails;
	}
}
