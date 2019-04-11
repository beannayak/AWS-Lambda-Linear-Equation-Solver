package com.rasal.awslambda.errorhandling;

public enum ErrorCode {

	INPUT_VALIDATION(100000, "Input validation error.", ErrorType._400),
	UNPARSABLE_JSON(100001, "Invalid JSON provided.", ErrorType._400),
	SERVER_ERROR(100002, "Server Error", ErrorType._500),
	INFEASIBLE_ERROR(100003, "Infeasible Request", ErrorType._400);
	
	public int errorCode;
	public String message;
	public ErrorType errorType;
	
	private ErrorCode(int errorCode, String message, ErrorType errorType) {
		this.errorCode = errorCode;
		this.message = message;
		this.errorType = errorType;
	}
	
	public static ErrorCode fromCode(int code) {
		for (ErrorCode ec : ErrorCode.values()) {
			if (ec.errorCode == code) {
				return ec;
			}
		}
		return null;
	}
}
