package com.rasal.awslambda.lindoutils;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OptimizationType {
	MAXIMIZE,
	MINIMIZE;
	
	@JsonCreator
	public static OptimizationType toEnum(String aString) {
		try {
			return OptimizationType.valueOf(aString);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}
