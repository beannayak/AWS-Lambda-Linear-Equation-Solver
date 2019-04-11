package com.rasal.awslambda.errorhandling;

import java.util.List;

public class Errors extends RuntimeException {

	private static final long serialVersionUID = 1000l;
	
	private List<APIError> errors;

	public Errors(List<APIError> errors) {
		this.errors = errors;
	}
	
	public List<APIError> getErrors() {
		return errors;
	}

	public void setErrors(List<APIError> errors) {
		this.errors = errors;
	}
}
