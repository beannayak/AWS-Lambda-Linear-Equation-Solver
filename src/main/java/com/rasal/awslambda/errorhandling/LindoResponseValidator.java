package com.rasal.awslambda.errorhandling;

import java.util.Arrays;

import org.apache.log4j.Logger;

import com.lindo.Lindo;
import com.springmock.Component;

@Component
public class LindoResponseValidator {
	private static final Logger LOG = Logger.getLogger(LindoResponseValidator.class);
	
	public void validateLindoCreatedObjectAndResponse(Object objectCreatedByLindo, int lindoResponseCode, String errorMessage) {
		if (objectCreatedByLindo == null) {
			throwServerError(errorMessage);
		}

		validateLindoResponse(lindoResponseCode, errorMessage);
	}
	
	private void throwServerError(String errorMessage) {
		LOG.error("Error Message: " + errorMessage + ", ErrorCode: " + ErrorCode.SERVER_ERROR.errorCode);
		
		APIError error = new APIError();
		error.setErrorCode(ErrorCode.SERVER_ERROR.errorCode);
		error.setMessage(errorMessage);
		throw new Errors(Arrays.asList(error));
	}

	public void validateLindoResponse(int lindoResponseCode, String errorMessage) {
		if (lindoResponseCode != Lindo.LSERR_NO_ERROR) {
			throwServerError(errorMessage);
		}
	}

	public void validateFeasibleResponse(int solutionStatus) {
		if (solutionStatus != Lindo.LS_STATUS_BASIC_OPTIMAL && solutionStatus != Lindo.LS_STATUS_OPTIMAL) {
			LOG.error("Error Message: infeasible solution , ErrorCode: " + ErrorCode.INFEASIBLE_ERROR.errorCode);
			
			APIError error = new APIError();
			error.setErrorCode(ErrorCode.INFEASIBLE_ERROR.errorCode);
			error.setMessage("Optimal solution can't be found.");
			
			throw new Errors(Arrays.asList(error));
		}
	}
}
