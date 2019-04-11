package com.rasal.awslambda;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rasal.awslambda.errorhandling.ErrorCode;
import com.rasal.awslambda.errorhandling.ErrorType;
import com.rasal.awslambda.errorhandling.Errors;
import com.rasal.awslambda.lindoutils.LinearEquationsBuilder;
import com.rasal.lindowrapper.LindoEnvironmentSetup;
import com.rasal.lindowrapper.model.solver.JsonResponseCreator;
import com.rasal.lindowrapper.model.solver.RuleSolver;
import com.rasal.lindowrapper.model.solver.RequestToProblemDefnCreator;
import com.springmock.Autowired;
import com.springmock.Component;

@Component
public class OptimizationResource {
	
	@Autowired
	private LindoEnvironmentSetup environmentCreator;
	
	@Autowired
	private RequestToProblemDefnCreator problemDefinitionCreator;
	
	@Autowired
	private RuleSolver newRuleSolver;
	
	@Autowired
	private JsonResponseCreator responseJsonCreator;
	
	public Response optimize(OptimizationRequest request) throws JsonProcessingException {
		
		try {
			LinearEquationsBuilder builder = problemDefinitionCreator.create(request);
			Map<String, Double> lindoResult = newRuleSolver.solve(builder, request.getObjectiveFunction());
			String responseString = responseJsonCreator.createResponse(lindoResult);
			
			return Response.from(200, responseString);
		} catch (Errors e) {
			String responseString = new ObjectMapper().writeValueAsString(e.getErrors());
			
			if (ErrorType._400.equals(ErrorCode.fromCode(e.getErrors().get(0).getErrorCode()).errorType)) {
				return Response.from(400, responseString);
			} 
			return Response.from(500, responseString);
		}
	}
}
