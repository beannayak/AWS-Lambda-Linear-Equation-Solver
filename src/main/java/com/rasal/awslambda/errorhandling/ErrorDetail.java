package com.rasal.awslambda.errorhandling;

public class ErrorDetail {
	private String path;
	private String message;
	private String invalidValue;
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getInvalidValue() {
		return invalidValue;
	}
	
	public void setInvalidValue(String invalidValue) {
		this.invalidValue = invalidValue;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
}
