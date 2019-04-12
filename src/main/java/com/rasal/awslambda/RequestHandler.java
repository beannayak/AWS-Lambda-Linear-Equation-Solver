package com.rasal.awslambda;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rasal.awslambda.errorhandling.APIError;
import com.rasal.awslambda.errorhandling.ErrorCode;
import com.rasal.awslambda.errorhandling.ErrorDetail;
import com.rasal.awslambda.errorhandling.Errors;
import com.springmock.ReflectionAutowirer;

public class RequestHandler {
	private static final Logger LOG = Logger.getLogger(RequestHandler.class);
	private static final OptimizationResource resource = ReflectionAutowirer.getAutowiredInstance(OptimizationResource.class);
	private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private static Validator validator = factory.getValidator();
	
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		String request = "{\"direction\":\"MINIMIZE\", \"objectiveFunction\":\"1.0_b\", \"constraints\": [ \"1.0_a;1.0_b=10.0\", \"1.0_a<3.0\" ] }";
		
		AWSRequest r = new AWSRequest();
		r.body = request;
		
		Response response = new RequestHandler().handler(r);
		System.out.println(response.body + ", status: " + response.statusCode);
	}
	
	public Response handler(AWSRequest request) throws JsonProcessingException {		
		LOG.info("Input received: " + request.body + ", serverless version: 1.0.0");
		
		ObjectMapper om = new ObjectMapper();
		OptimizationRequest optRequest;
		try {
			optRequest = om.readValue(request.body, OptimizationRequest.class);
		} catch (IOException e) {
			APIError e1 = new APIError();
			e1.setErrorCode(ErrorCode.UNPARSABLE_JSON.errorCode);
			e1.setMessage(ErrorCode.UNPARSABLE_JSON.message);
			return Response.from(400, om.writeValueAsString(new Errors(Arrays.asList(e1))));
		}
		
		String validationMessage = validateAnnotations(optRequest);
		if (!"PASSED".equals(validationMessage)) {
			return Response.from(400, validationMessage);
		}
		
		return resource.optimize(optRequest);
	}

	private String validateAnnotations(OptimizationRequest optRequest) throws JsonProcessingException {
		Set<ConstraintViolation<OptimizationRequest>> violations = validator.validate(optRequest);
		if (!violations.isEmpty()) {
			APIError e = new APIError();
			List<ErrorDetail> errorDetails =
				violations.stream()
					.map(violation -> {
						ErrorDetail ed = new ErrorDetail();
						ed.setPath("input." + violation.getPropertyPath().toString());
						ed.setMessage(violation.getMessage());
						ed.setInvalidValue(violation.getInvalidValue() != null ? violation.getInvalidValue().toString() : "null");
						
						return ed;
				}).collect(Collectors.toList());
			e.setErrorCode(ErrorCode.INPUT_VALIDATION.errorCode);
			e.setMessage(ErrorCode.INPUT_VALIDATION.message);
			e.setErrorDetails(errorDetails);
			
			ObjectMapper om = new ObjectMapper();
			return om.writeValueAsString(new Errors(Arrays.asList(e)).getErrors());
		}
		
		return "PASSED";
	}
}
